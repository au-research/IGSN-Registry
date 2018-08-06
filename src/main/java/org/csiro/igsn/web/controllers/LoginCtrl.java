package org.csiro.igsn.web.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.Allocator;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.AllocatorEntityService;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.csiro.igsn.exception.ExceptionWrapper;
import org.csiro.igsn.security.LdapUser;
import org.csiro.igsn.security.RegistryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


@Controller
public class LoginCtrl {
	
	static final Logger log = Logger.getLogger(LoginCtrl.class);
	@RequestMapping("getUser.do")
	public ResponseEntity<Object>  user(Principal user) {
		if(user != null) {
			RegistryUser r = new RegistryUser(user.getName());
			if (r != null) {
				r.setPassword("HIDDEN");
				return new ResponseEntity<Object>(r, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(new ExceptionWrapper("Login","Unauthorized Access") ,HttpStatus.UNAUTHORIZED);
	}

	@Value("#{configProperties['AAF_RAPID_URL']}")
	private String AAF_RAPID_URL;

	@RequestMapping("getAAF.do")
	public ResponseEntity<Map<String, String>> aaf() {
		Map<String, String> details = new HashMap<>();
		details.put("AAF_RAPID_URL", AAF_RAPID_URL);
		return new ResponseEntity<>(details, HttpStatus.OK);
	}

	@RequestMapping("auscope_login")
	public ResponseEntity<Object> redirectToExternalUrl() throws URISyntaxException {

		URI aaf_login = new URI(AAF_RAPID_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(aaf_login);
		return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
	}

}
