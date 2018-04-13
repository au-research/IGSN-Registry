package org.csiro.igsn.web.controllers;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.csiro.igsn.exception.ExceptionWrapper;
import org.csiro.igsn.security.LdapUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class LoginCtrl {
	
	static final Logger log = Logger.getLogger(LoginCtrl.class);
	@RequestMapping("getUser.do")
	public ResponseEntity<Object>  user(Principal user) {
		if(user != null){
			String userName = user.getName();
			RegistrantEntityService registerantEntityService = new RegistrantEntityService(null, null);
			Registrant r = registerantEntityService.searchRegistrant(userName);
			return new ResponseEntity<Object>(r,HttpStatus.OK);
		}else{
			return new ResponseEntity<Object>(new ExceptionWrapper("Login","Unauthorized Access") ,HttpStatus.UNAUTHORIZED);
		}
	}
	


}
