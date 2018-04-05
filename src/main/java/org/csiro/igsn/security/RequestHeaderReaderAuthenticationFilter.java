package org.csiro.igsn.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.csiro.igsn.entity.LoginRequest;
import org.apache.commons.io.IOUtils;
import org.csiro.igsn.entity.mysql.UserDAO;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestHeaderReaderAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    final Logger log = Logger.getLogger( RequestHeaderReaderAuthenticationFilter.class);

    private static final String ERROR_MESSAGE = "Something went wrong while parsing /login request body";

    private RegistrantEntityService registrantEntityService;


    public RequestHeaderReaderAuthenticationFilter() {
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

            String referer = request.getHeader("Referer");
        log.info("REFERER URL" + referer);
        if (referer == null || referer.isEmpty()){
            throw new UsernameNotFoundException("Invalid username/password");
        }
        this.registrantEntityService = new RegistrantEntityService(null, null);
        Registrant r = registrantEntityService.searchRegistrantByRefererUrls(referer);
        if(r == null){
            throw new UsernameNotFoundException("Invalid username/password");
        }
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(r.getUsername(), r.getPassword());
        setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);

    }
}