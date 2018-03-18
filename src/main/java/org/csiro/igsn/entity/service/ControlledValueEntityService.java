package org.csiro.igsn.entity.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.csiro.igsn.entity.postgres.CvIdentifierType;
import org.csiro.igsn.entity.postgres.CvMaterialTypes;
import org.csiro.igsn.entity.postgres.CvResourceType;
import org.csiro.igsn.entity.postgres.Registrant;
import org.springframework.stereotype.Service;

@Service
public class ControlledValueEntityService {
	

	
	

	
	
	public CvResourceType searchResourceType(String resourceType){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			CvResourceType cvResourceType = em.createNamedQuery("CvResourceType.search",CvResourceType.class)
				.setParameter("resourceType", resourceType)
				.getSingleResult();			 		 
			 return cvResourceType;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}
	
	public List<CvResourceType> listResourceType(){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			List<CvResourceType> cvResourceTypes = em.createNamedQuery("CvResourceType.listAll",CvResourceType.class)
					.getResultList();									 		 
			 return cvResourceTypes;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}


	public List<CvMaterialTypes> listMaterialTypes() {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			List<CvMaterialTypes> cvMaterialTypes = em.createNamedQuery("CvMaterialTypes.listAll",CvMaterialTypes.class)
					.getResultList();									 		 
			 return cvMaterialTypes;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}


	public List<CvIdentifierType> listIdentifierType() {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			List<CvIdentifierType> cvIdentifierType = em.createNamedQuery("CvIdentifierType.listAll",CvIdentifierType.class)
					.getResultList();									 		 
			 return cvIdentifierType;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	
}
