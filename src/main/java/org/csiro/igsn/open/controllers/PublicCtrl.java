package org.csiro.igsn.open.controllers;


import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.Prefix;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.postgres.Resources;
import org.csiro.igsn.entity.service.PrefixEntityService;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.csiro.igsn.entity.service.ResourceEntityService;
import org.csiro.igsn.exception.ExceptionWrapper;
import org.csiro.igsn.utilities.IGSNUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/public/")
public class PublicCtrl {
	
	static final Logger log = Logger.getLogger(PublicCtrl.class);
	
	private ResourceEntityService resourceEntityService; 
	RegistrantEntityService registerantEntityService;
	
	@Autowired
	public PublicCtrl(ResourceEntityService resourceEntityService,RegistrantEntityService registerantEntityService){
		this.resourceEntityService = resourceEntityService;
		this.registerantEntityService = registerantEntityService;
	}
	

	
	@RequestMapping("getResource.do")
	public ResponseEntity<Object> getResource(
			@RequestParam(required = true, value ="resourceIdentifier") String resourceIdentifier,
			Principal user) {
		
		try {
			Resources r = this.resourceEntityService.searchResourceByIdentifier(resourceIdentifier);
			if(r==null){
				throw new NullPointerException("Resource not found");
			}
			
			if(!r.getIsPublic() && user == null){
				return new ResponseEntity<Object>(new ExceptionWrapper("Login","Unauthorized Access") ,HttpStatus.UNAUTHORIZED);
			}else if(!r.getIsPublic() && user!=null){
				Registrant registrant = this.registerantEntityService.searchActiveRegistrantAndPrefix(user.getName());				
				if(IGSNUtil.stringStartsWithAllowedPrefix(registrant.getPrefixes(), r.getResourceIdentifier())){
					return new ResponseEntity<Object>(r,HttpStatus.OK);
				}else{
					return new ResponseEntity<Object>(new ExceptionWrapper("Permission Denied","Only prefix owner can view " + r.getResourceIdentifier()) ,HttpStatus.FORBIDDEN);
				}
								
			}else{
				return new ResponseEntity<Object>(r,HttpStatus.OK);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Fail to retrieve resource",e.toString()),HttpStatus.BAD_REQUEST);
		}
		
		
	}
	

	
	
	
}
