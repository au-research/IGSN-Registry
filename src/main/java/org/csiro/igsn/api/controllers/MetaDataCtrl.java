package org.csiro.igsn.api.controllers;

import java.security.Principal;
import java.util.List;
import org.csiro.igsn.entity.service.ResourceEntityService;
import org.csiro.igsn.jaxb.oai.bindings.JAXBConverterInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/metadata/30")
public class MetaDataCtrl {
	
	static final Logger log = Logger.getLogger(MetaDataCtrl.class);
	ResourceEntityService resourceEntityService;
	List<JAXBConverterInterface> converters;
	
	@Autowired
	public MetaDataCtrl(ResourceEntityService resourceEntityService,List<JAXBConverterInterface> converters){
		this.resourceEntityService = resourceEntityService;
		this.converters = converters;
	}
	
	private JAXBConverterInterface getSupportedConverter(String schema){
		for(JAXBConverterInterface c:converters){
			if(c.supports(schema)){
				return c;
			}
		}
		return null;
	}
	
	
	@RequestMapping(value = "/retrieve/{resourceIdentifier}")
	public ResponseEntity getMetadataByIGSN(@PathVariable("resourceIdentifier") String resourceIdentifier,
			@RequestParam(required = false, value ="schema") String schema,
			Principal user) {
		ResponseEntity<? extends Object> response = null;
		log.info("Get Metadata By Resource identifier : " + resourceIdentifier);		
		if (user!=null) {			
			try {
				JAXBConverterInterface converter = getSupportedConverter(schema);
				if(converter==null){
					converter = new org.csiro.igsn.jaxb.oai.bindings.csiro.EntityToSchemaConverterCSIRO();
				}
				response = resourceEntityService.getResourceMetadataByIdentifier(resourceIdentifier,converter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			response = new ResponseEntity<String>("no login", new HttpHeaders(),HttpStatus.UNAUTHORIZED);
		}
		log.info("Get Metadata By Resource Identifier - Response code : " + response.getStatusCode());

		return response;
	}

}
