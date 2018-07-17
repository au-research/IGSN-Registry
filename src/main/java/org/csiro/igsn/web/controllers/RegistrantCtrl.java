package org.csiro.igsn.web.controllers;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.Prefix;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.ControlledValueEntityService;
import org.csiro.igsn.entity.service.PrefixEntityService;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.csiro.igsn.entity.service.ResourceEntityService;
import org.csiro.igsn.exception.ExceptionWrapper;
import org.csiro.igsn.security.RegistryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/web/")
public class RegistrantCtrl {
	
	static final Logger log = Logger.getLogger(RegistrantCtrl.class);
	
	private RegistrantEntityService registrantEntityService; 
	private PrefixEntityService prefixEntityService;
	
	@Autowired
	public RegistrantCtrl(RegistrantEntityService registrantEntityService, PrefixEntityService prefixEntityService){
		this.registrantEntityService = registrantEntityService;
		this.prefixEntityService = prefixEntityService;
	}
	
	@RequestMapping("listRegistrants.do")
	public ResponseEntity<List<Registrant>> listRegistrants(Principal user) {
		
		return new ResponseEntity<List<Registrant>>(this.registrantEntityService.listRegistrant(),HttpStatus.OK);
		
	}

	@RequestMapping("getAllRecords.do")
	public ResponseEntity<Object> getAllRecords(Principal user) {
		ResourceEntityService re = new ResourceEntityService(new ControlledValueEntityService());
		Registrant r = this.registrantEntityService.searchActiveRegistrantAndPrefix(user.getName());
		if (r == null) {
			return new ResponseEntity<>(new String[0], HttpStatus.OK);
		}

		try {
			return new ResponseEntity<>(re.getResourceMetadataByRegistrant(r), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.OK);
		}
	}


	@RequestMapping("removeRegistrants.do")
	public ResponseEntity<Object> removeRegistrants(
			@RequestParam(required = true, value ="username") String username,
			Principal user) {
		
		try {
			this.registrantEntityService.removeRegistrant(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Fail to remove registrant",e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(this.registrantEntityService.listRegistrant(),HttpStatus.OK);
		
	}
	

	@RequestMapping("listPrefix.do")
	public ResponseEntity<Object> listPrefix(Principal user) {		
		return new ResponseEntity<Object>(this.prefixEntityService.listAllPrefix(),HttpStatus.OK);		
	}
	
	@RequestMapping("getAllocatedPrefix.do")
	public ResponseEntity<Object> getAllocatedPrefix(Principal user) {
		Registrant r = this.registrantEntityService.searchActiveRegistrantAndPrefix(user.getName());
		if(r != null){
			return new ResponseEntity<Object>(this.registrantEntityService.searchActiveRegistrantAndPrefix(user.getName()).getPrefixes(),HttpStatus.OK);
		}
		else{
			return new ResponseEntity<Object>(new String[0],HttpStatus.OK);
		}
		
	}
	
	@RequestMapping("allocatePrefix.do")
	public ResponseEntity<Object> allocatePrefix(
			@RequestParam(required = true, value ="prefix") String prefix,
			@RequestParam(required = true, value ="username") String username,
			Principal user) throws Exception {
		this.registrantEntityService.allocatePrefix(prefix,username);
		return listPrefix(user);
	}
	
	
	@RequestMapping("unAllocatePrefix.do")
	public ResponseEntity<Object> unAllocatePrefix(
			@RequestParam(required = true, value ="prefix") String prefix,
			@RequestParam(required = true, value ="username") String username,
			Principal user){
		try {
			this.registrantEntityService.unAllocatePrefix(prefix,username);
		} catch (Exception e) {			
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Fail to unallocate prefix",e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		return listPrefix(user);
	}
	
	
	@RequestMapping("addRegistrant.do")
	public ResponseEntity<Object> addRegistrant(		
			@RequestParam(required = true, value ="email") String email,
			@RequestParam(required = true, value ="name") String name,
			@RequestParam(required = true, value ="username") String username,
			Principal user){
		try {
			this.registrantEntityService.addRegistrant(user, email, name, username);
		} catch (Exception e) {			
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Fail to add registrant",e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping("setRegistrantActive.do")
	public ResponseEntity<Object> setRegistrantActive(		
			@RequestParam(required = true, value ="active") boolean active,		
			@RequestParam(required = true, value ="username") String username,
			Principal user){
		try {
			this.registrantEntityService.setActiveRegistrant(username, active);
		} catch (Exception e) {			
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Fail to change registrant status",e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping("addPrefix.do")
	public ResponseEntity<Object> addPrefix(		
			@RequestParam(required = true, value ="description") String description,		
			@RequestParam(required = true, value ="prefix") String prefix,
			Principal user){
		try {
			this.prefixEntityService.addPrefix(user, description, prefix);
		} catch (Exception e) {			
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Fail to add new prefix",e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping("setTC.do")
	public ResponseEntity<Object>  user(
		@RequestParam(required = true, value ="username") String username,
		Principal user){
			try {
				this.registrantEntityService.setTCAccepted(username, true);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Object>(new ExceptionWrapper("Failed to set tc_accepted",e.getMessage()),HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
