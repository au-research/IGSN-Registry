package org.csiro.igsn.entity.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.Allocator;
import org.csiro.igsn.entity.postgres.Prefix;
import org.csiro.igsn.entity.postgres.RefererUrls;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.web.controllers.RegistrantCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegistrantEntityService {

	PrefixEntityService prefixEntityService;
	AllocatorEntityService allocatorEntityService;
    @Value("#{configProperties['IGSN_NEW_REGISTRANT_DEFAULT_PASSWORD']}")
    private String IGSN_NEW_REGISTRANT_DEFAULT_PASSWORD;

	static final Logger log = Logger.getLogger(RegistrantEntityService.class);
	@Autowired
	public RegistrantEntityService(PrefixEntityService prefixEntityService,AllocatorEntityService allocatorEntityService){
		this.prefixEntityService = prefixEntityService;
		this.allocatorEntityService = allocatorEntityService;

	}


	public Registrant searchRegistrantAndPrefix(String user){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			Registrant registrant = em.createNamedQuery("Registrant.searchByUsernameJoinPrefix",Registrant.class)
					.setParameter("username", user)
					.getSingleResult();
			return registrant;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	public Registrant searchActiveRegistrantAndPrefix(String user){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			log.debug("IsearchActiveRegistrantAndPrefix:" + user.toString());
			Registrant registrant = em.createNamedQuery("Registrant.searchActiveByUsernameJoinPrefix",Registrant.class)
					.setParameter("username", user)
					.getSingleResult();
			return registrant;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	public Registrant searchRegistrant(String user){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			Registrant registrant = em.createNamedQuery("Registrant.searchByUsername",Registrant.class)
					.setParameter("username", user)
					.getSingleResult();
			return registrant;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	public Registrant searchRegistrantByRefererUrls(String url){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			log.debug("searchRegistrantByRefererUrls:" + url);
			RefererUrls refererUrls = em.createNamedQuery("RefererUrls.searchByRefererUrl",RefererUrls.class)
					.setParameter("referer_url", url)
					.getSingleResult();
			Registrant registrant = this.getRegistrantbyID(refererUrls.getRegistrantid());
			return registrant;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}


	public Registrant getRegistrantbyID(int id){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			log.debug("searchRegistrantByRefererUrls:" + id);
			Registrant registrant = em.createNamedQuery("Registrant.getRegistrantById",Registrant.class)
					.setParameter("id", id)
					.getSingleResult();
			return registrant;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}


	public List<Registrant> listRegistrant(){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			List<Registrant> registrant = em.createNamedQuery("Registrant.getAllRegistrant",Registrant.class)
					.getResultList();
			return registrant;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	public boolean allocatePrefix(String prefix, String username) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{

			boolean allowed = false;
			Registrant registrantEntity = searchRegistrant(username);
			for(Prefix allocatorPrefix:registrantEntity.getAllocator().getPrefixes()){
				if(prefix.startsWith(allocatorPrefix.getPrefix().toUpperCase())){
					allowed = true;
				}
			}
			em.getTransaction().begin();
			if(allowed){
				Prefix prefixEntity = this.prefixEntityService.searchJoinRegistrant(prefix);
				prefixEntity.getRegistrants().add(registrantEntity);
				em.merge(prefixEntity);
				em.flush();
				em.getTransaction().commit();
				return true;
			}else{
				throw new Exception("Prefix is not allowed as it does not match allocator's prefixes.");

			}
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}finally{
			em.close();
		}

	}


	public boolean unAllocatePrefix(String prefix, String username) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{

			boolean allowed = false;
			Registrant registrantEntity = searchRegistrant(username);
			for(Prefix allocatorPrefix:registrantEntity.getAllocator().getPrefixes()){
				if(prefix.startsWith(allocatorPrefix.getPrefix().toUpperCase())){
					allowed = true;
				}
			}
			em.getTransaction().begin();
			if(allowed){
				Prefix prefixEntity = this.prefixEntityService.searchJoinRegistrant(prefix);

				for(Registrant r:prefixEntity.getRegistrants()){
					if(r.getRegistrantid()==registrantEntity.getRegistrantid()){
						prefixEntity.getRegistrants().remove(r);
						break;
					}
				}
				em.merge(prefixEntity);
				em.flush();
				em.getTransaction().commit();
				return true;
			}else{
				throw new Exception("Prefix is not allowed as it does not match allocator's prefixes.");
			}
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}finally{
			em.close();
		}

	}


	public void removeRegistrant(String username) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			em.getTransaction().begin();

			Registrant registrantEntity = searchRegistrantAndPrefix(username);
			if(registrantEntity.getPrefixes()==null || registrantEntity.getPrefixes().isEmpty()){
				em.remove(em.contains(registrantEntity) ? registrantEntity : em.merge(registrantEntity));
				em.getTransaction().commit();
			}else{
				throw new Exception("Registrant cannot be deleted as it has prefixes assigned to it.");
			}


		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}finally{
			em.close();
		}

	}

	public boolean setActiveRegistrant(String username,boolean active) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			em.getTransaction().begin();
			Registrant registrantEntity = searchRegistrantAndPrefix(username);
			registrantEntity.setIsactive(active);
			em.merge(registrantEntity);
			em.flush();
			em.getTransaction().commit();
			return true;
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}finally{
			em.close();
		}

	}

	public boolean addRegistrant(Principal user,String email,String name,String username) throws Exception {
        EntityManager em = JPAEntityManager.createEntityManager();
        try {
            em.getTransaction().begin();
            Allocator allocator = null;
            Registrant registrant = new Registrant();
            allocator = this.allocatorEntityService.searchAllocator(user.getName());
            registrant.setPassword(IGSN_NEW_REGISTRANT_DEFAULT_PASSWORD);
            registrant.setAllocator(allocator);
            registrant.setCreated(new Date());
            registrant.setIsactive(true);
            registrant.setRegistrantemail(email);
            registrant.setRegistrantname(name);
            registrant.setUpdated(new Date());
            registrant.setUsername(username);
            em.merge(registrant);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error("AddRegistrant error:" + errors);
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean addRegistrant(String AllocatorName,  String email,String name,String username) throws Exception {
        EntityManager em = JPAEntityManager.createEntityManager();
        try{
            em.getTransaction().begin();
            Allocator allocator = null;
            Registrant registrant = new Registrant();
            allocator = this.allocatorEntityService.searchAllocator(AllocatorName);
            registrant.setPassword("NOT USED");
            registrant.setAllocator(allocator);
            registrant.setCreated(new Date());
            registrant.setIsactive(true);
            registrant.setRegistrantemail(email);
            registrant.setRegistrantname(name);
            registrant.setUpdated(new Date());
            registrant.setUsername(username);
            em.merge(registrant);
            em.flush();
            em.getTransaction().commit();
            return true;
        }catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error("AddRegistrant error:" + errors);
            em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
    }





}
