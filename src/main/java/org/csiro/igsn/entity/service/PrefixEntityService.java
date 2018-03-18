package org.csiro.igsn.entity.service;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;











import javax.persistence.PersistenceException;

import org.csiro.igsn.entity.postgres.Allocator;
import org.csiro.igsn.entity.postgres.Prefix;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource;
import org.csiro.igsn.utilities.IGSNUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PrefixEntityService {
	
	public List<Prefix> listAllPrefix(){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<Prefix> result = em.createNamedQuery("Prefix.listAll",Prefix.class)	 
	    .getResultList();
		em.close();		
		return result;
	}
	

	public boolean addPrefix(Principal user,String description,String prefix) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();		
		try{	
			
			em.getTransaction().begin();
			Registrant registrant = em.createNamedQuery("Registrant.searchActiveByUsernameJoinPrefix",Registrant.class)
					.setParameter("username", user.getName())
					.getSingleResult();	
			
			Allocator allocator = registrant.getAllocator();
			if(!IGSNUtil.stringStartsWithAllowedPrefix(allocator.getPrefixes(), prefix)){
				throw new Exception("Prefix not supported by allocator");
			}				
			Prefix prefixEntity = new Prefix();
			prefixEntity.setCreated(new Date());
			prefixEntity.setDescription(description);
			prefixEntity.setPrefix(prefix.toUpperCase());
			prefixEntity.setVersion(1);
			
			em.merge(prefixEntity);
			em.flush();
			em.getTransaction().commit();
			return true;
		}catch(PersistenceException pe){
			em.getTransaction().rollback();			
			throw new PersistenceException("Duplicate prefix");
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}finally{
			em.close();
		}
		
	}
	
	
	
	public void persist(Prefix rs){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(rs);
		em.flush();
		em.getTransaction().commit();
	    em.close();	    
	}
	
	
	
	public Prefix search(String prefix){
		try{
			EntityManager em = JPAEntityManager.createEntityManager();
			Prefix result = em.createNamedQuery("Prefix.search",Prefix.class)
		    .setParameter("prefix", prefix)
		    .getSingleResult();
			 em.close();			 
			 return result;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}
	}
	
	public Prefix searchJoinRegistrant(String prefix){
		try{
			EntityManager em = JPAEntityManager.createEntityManager();
			Prefix result = em.createNamedQuery("Prefix.searchJoinRegistrant",Prefix.class)
		    .setParameter("prefix", prefix)
		    .getSingleResult();
			 em.close();			 
			 return result;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	
	
}
