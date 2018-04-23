package org.csiro.igsn.entity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.AlternateIdentifiers;
import org.csiro.igsn.entity.postgres.Classifications;
import org.csiro.igsn.entity.postgres.Contributors;
import org.csiro.igsn.entity.postgres.CurationDetails;
import org.csiro.igsn.entity.postgres.JAXBResourceToEntityConverter;
import org.csiro.igsn.entity.postgres.LogDate;
import org.csiro.igsn.entity.postgres.MaterialTypes;
import org.csiro.igsn.entity.postgres.Method;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.postgres.RelatedResources;
import org.csiro.igsn.entity.postgres.ResourceDate;
import org.csiro.igsn.entity.postgres.ResourceTypes;
import org.csiro.igsn.entity.postgres.Resources;
import org.csiro.igsn.entity.postgres.SampledFeatures;
import org.csiro.igsn.jaxb.oai.bindings.JAXBConverterInterface;
import org.csiro.igsn.jaxb.oai.bindings.csiro.Resources.Resource.Location;
import org.csiro.igsn.jaxb.registration.bindings.EventType;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource;
import org.csiro.igsn.web.controllers.LoginCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResourceEntityService {

	JAXBResourceToEntityConverter jaxbResourceToEntityConverter;
	static final Logger log = Logger.getLogger(ResourceEntityService.class);

	public static final int PAGING_SIZE=10;

	@Autowired
	public ResourceEntityService(ControlledValueEntityService controlledValueEntityService){
		this.jaxbResourceToEntityConverter = new JAXBResourceToEntityConverter(controlledValueEntityService);
	}

	public Long getResourcesSizeByDate(Date fromDate, Date until) {

		EntityManager em = JPAEntityManager.createEntityManager();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<Resources> from = countQuery.from(Resources.class);

		List<Predicate> predicates =this.oaiPredicateBuilder(fromDate,until, criteriaBuilder,from);

		CriteriaQuery<Long> select = countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

		TypedQuery<Long> typedQuery = em.createQuery(select);

		Long result = typedQuery.getSingleResult();

		em.close();
		return result;
	}

	public void checkEmbargo(){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		Query q = em.createNativeQuery("update version30.resources set is_public=true,modified=now() where is_public=false and embargo_end is not null and embargo_end < now()");
		q.executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

	public List<Resources> searchSampleByDate(Date fromDate, Date until, Integer pageNumber){
		checkEmbargo();
		final Integer pageSize = getPagingSize();

		EntityManager em = JPAEntityManager.createEntityManager();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Resources> criteriaQuery = criteriaBuilder.createQuery(Resources.class);
		Root<Resources> from = criteriaQuery.from(Resources.class);

		from.fetch("location",JoinType.LEFT);
		from.fetch("logDate", JoinType.LEFT);
		from.fetch("method", JoinType.LEFT);
		from.fetch("resourceDate", JoinType.LEFT);
		from.fetch("contributorses", JoinType.LEFT).fetch("cvIdentifierType",JoinType.LEFT);
		from.fetch("relatedResourceses", JoinType.LEFT).fetch("cvIdentifierType",JoinType.LEFT);
		from.fetch("alternateIdentifierses", JoinType.LEFT);
		from.fetch("classificationses", JoinType.LEFT);
		from.fetch("resourceTypeses", JoinType.LEFT).fetch("cvResourceType",JoinType.LEFT);
		from.fetch("sampledFeatureses", JoinType.LEFT);
		from.fetch("curationDetailses", JoinType.LEFT);
		from.fetch("materialTypeses", JoinType.LEFT).fetch("cvMaterialTypes",JoinType.LEFT);


		List<Predicate> predicates =this.oaiPredicateBuilder(fromDate,until, criteriaBuilder,from);

		CriteriaQuery<Resources> select = criteriaQuery.select(from).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

		select = select.orderBy(criteriaBuilder.asc(from.get("resourceid")));

		TypedQuery<Resources> typedQuery = em.createQuery(select);

		if(pageNumber != null && pageSize != null){
			typedQuery.setFirstResult((pageNumber)*pageSize);
			typedQuery.setMaxResults(pageSize);
		}

		List<Resources> result = typedQuery.getResultList();

		em.close();
		return result;
	}


	private List<Predicate> oaiPredicateBuilder(Date from, Date until,CriteriaBuilder criteriaBuilder,Root<Resources> fromTable){

		List<Predicate> predicates = new ArrayList<Predicate>();

		//VT: we are only keen in public date for oai harvesting
		predicates.add(criteriaBuilder.isTrue(fromTable.get("isPublic")));

		if (from != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(fromTable.get("modified"),from));
		}

		if (until != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(fromTable.get("modified"),until));
		}


		return predicates;
	}



	public void insertResource(Resource resourceXml,Registrant registrant,boolean isWebInsert) throws Exception {

		EntityManager em = JPAEntityManager.createEntityManager();
		Resources resourcesEntity = new Resources();
		try{
			em.getTransaction().begin();
			jaxbResourceToEntityConverter.convert(resourceXml,registrant,resourcesEntity);
			if(isWebInsert){
				resourcesEntity.setInputMethod("form");
			}
			em.persist(resourcesEntity);
			em.flush();
			em.getTransaction().commit();
			em.close();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}

	}





	public void destroyResource(Resource resource,boolean isWebInsert) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			em.getTransaction().begin();
			Resources r = this.searchResourceByIdentifier(resource.getResourceIdentifier().getValue());
			if(r==null){
				throw new Exception("Resource not found, unable to update resource. Change event type to registered");
			}
			if(isWebInsert){
				r.setInputMethod("form");
			}
			r.getLogDate().setEventType((EventType.DESTROYED.value()));
			r.setModified(new Date());
			em.merge(r);
			em.flush();
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}finally{
			if(em.isOpen()){
				em.close();
			}
		}

	}

	public void deprecateResource(Resource resource, boolean isWebInsert) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			em.getTransaction().begin();
			Resources r = this.searchResourceByIdentifier(resource.getResourceIdentifier().getValue());
			if(r==null){
				throw new Exception("Resource not found, unable to update resource. Change event type to registered");
			}
			if(isWebInsert){
				r.setInputMethod("form");
			}
			r.getLogDate().setEventType((EventType.DEPRECATED.value()));
			r.setModified(new Date());
			em.merge(r);
			em.flush();
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}finally{
			if(em.isOpen()){
				em.close();
			}
		}

	}





	public void updateResource(Resource resourceXML, Registrant registrant, boolean isWebInsert) throws Exception {
		log.info("Registrant:" + registrant.getUsername());
		log.info("IDENTIFIER:" + resourceXML.getResourceIdentifier().getValue());
		EntityManager em = JPAEntityManager.createEntityManager();
		Resources resourcesEntity = this.searchResourceByIdentifier(resourceXML.getResourceIdentifier().getValue());

		if(resourcesEntity==null){
			throw new Exception("Resource " + resourceXML.getResourceIdentifier().getValue() + "not found, unable to update.");
		}

		if(!((Integer)registrant.getRegistrantid()).equals((Integer)resourcesEntity.getRegistrant().getRegistrantid())){
			throw new Exception("Only the Registrant who created the IGSN "+ resourceXML.getResourceIdentifier().getValue() +" Record can modify it.");
		}

		try{
			resourcesEntity = new Resources();
			em.getTransaction().begin();
			jaxbResourceToEntityConverter.convert(resourceXML,registrant,resourcesEntity);
			if(isWebInsert){
				resourcesEntity.setInputMethod("form");
			}
			resourcesEntity.getLogDate().setEventType((EventType.UPDATED.value()));
			em.merge(resourcesEntity);
			em.flush();
			em.getTransaction().commit();
			em.close();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}

	}

	public void testInsertResource(Resource resourceXML,Registrant registrant) throws Exception {

		if(!resourceXML.getLogDate().getEventType().equals(EventType.REGISTERED)){
			throw new Exception("You can only test insert with log event type = registered");
		}

		EntityManager em = JPAEntityManager.createEntityManager();
		Resources resourcesEntity = new Resources();
		try{
			em.getTransaction().begin();
			jaxbResourceToEntityConverter.convert(resourceXML,registrant,resourcesEntity);
			em.persist(resourcesEntity);
			em.flush();
			em.getTransaction().rollback();//VT: Because this is a test, it will always be rolled back
			em.close();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}
	}

	public int getPagingSize() {
		// TODO Auto-generated method stub
		return this.PAGING_SIZE;
	}




	public ResponseEntity<? extends Object> getResourceMetadataByIdentifier(String resourceIdentifier,JAXBConverterInterface converter) throws Exception {

		Resources resourceEntity= this.searchResourceByIdentifier(resourceIdentifier);
		if(resourceEntity==null){
			return new ResponseEntity<String>("IGSN does not exists in our database", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(converter.convert(resourceEntity), HttpStatus.OK);

	}



	public Resources searchResourceByIdentifier(String resourceIdentifier){
		checkEmbargo();
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			Resources result = em.createNamedQuery("Resources.searchByIdentifier",Resources.class)
					.setParameter("resourceIdentifier", resourceIdentifier)
					.getSingleResult();
			return result;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	public Resources searchResourceByIdentifierPublic(String resourceIdentifier){
		checkEmbargo();
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			Resources result = em.createNamedQuery("Resources.searchByIdentifierPublic",Resources.class)
					.setParameter("resourceIdentifier", resourceIdentifier)
					.getSingleResult();
			return result;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}






}
