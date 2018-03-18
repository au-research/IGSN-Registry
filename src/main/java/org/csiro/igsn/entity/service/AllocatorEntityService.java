package org.csiro.igsn.entity.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.csiro.igsn.entity.postgres.Allocator;
import org.springframework.stereotype.Service;

@Service
public class AllocatorEntityService {

	public Allocator searchAllocator(String user){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			Allocator allocator = em.createNamedQuery("Allocator.searchByUsername",Allocator.class)
				.setParameter("username", user)
				.getSingleResult();				
			return allocator;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}
	
	
	
	

	
}
