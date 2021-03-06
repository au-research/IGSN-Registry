package org.csiro.igsn.security.aaf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.AllocatorEntityService;
import org.csiro.igsn.entity.service.PrefixEntityService;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.csiro.igsn.security.BuiltInUserAuthenticationService;
import org.csiro.igsn.security.RegistrantAuthenticationService;
import org.csiro.igsn.security.aaf.AAFAuthentication;
import org.csiro.igsn.security.aaf.AAFJWT;
import org.csiro.igsn.security.RegistryUser;
import org.csiro.igsn.utilities.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.csiro.igsn.security.aaf.AAFAttributes;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * Created by wis056 on 7/04/2015.
 * Modified by woo392.
 */

@Component
public class JWTManagement {
    
    static private String AAF_PRODUCTION = "https://rapid.aaf.edu.au";
    static private String AAF_TEST = "https://rapid.test.aaf.edu.au";
    final Logger log = Logger.getLogger(JWTManagement.class);

    private String AAF_SECRET;

    @Value("#{configProperties['AAF_ROOT_SERVICE_URL']}")
    private String AAF_ROOT_SERVICE_URL;

    private RegistrantEntityService registrantEntityService;
    private AllocatorEntityService allocatorEntiryService;
    private PrefixEntityService prefixEntityService;

    public AAFAuthentication parseJWT(String tokenString) throws AuthenticationException {
        if (tokenString == null)
            throw new AuthenticationCredentialsNotFoundException("Unable to authenticate. No AAF credentials found.");

        AAF_SECRET = Config.getAAFSecret();

        if (AAF_SECRET == null)
            throw new AuthenticationCredentialsNotFoundException("Unable to authenticate. No AAF_SECRET defined.");

        MacSigner key = new MacSigner(AAF_SECRET.getBytes());
        Jwt jwt = JwtHelper.decodeAndVerify(tokenString, key);
        String claims = jwt.getClaims();

        ObjectMapper mapper = new ObjectMapper();
        try {
            log.debug("JWT Claims:" + claims);
           // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            AAFJWT token = mapper.readValue(claims, AAFJWT.class);
            log.info(("AAFJWT Token:  " + token.toString()));
            if (!(token.aafServiceUrl.equals(AAF_PRODUCTION) || token.aafServiceUrl.equals(AAF_TEST)))
                throw new AuthenticationServiceException("Unable to authenticate. The AAF URL does not match the expected " +
                        "value for the test or production AAF Rapid Connect services. Expected " + AAF_PRODUCTION +
                        " or " + AAF_TEST + " but got " + token.aafServiceUrl);

//            if (!(token.localServiceUrl.equals(AAF_ROOT_SERVICE_URL)))
//                throw new AuthenticationServiceException("Unable to authenticate. The URL of this server, "
//                        + AAF_ROOT_SERVICE_URL +
//                        " does not match the URL registered with AAF " + token.localServiceUrl + " .");

            Date now = new Date();
            if (now.before(token.notBefore))
                throw new AuthenticationServiceException("Unable to authenticate. The authentication is " +
                        "marked to not be used before the current date, " + now.toString());

            if (token.expires.before(now))
                throw new AuthenticationServiceException("Unable to authenticate. The authentication has expired. " +
                        "Now: " + now.toString() + " expired: " + token.expires.toString() );

            // XXX We're not currently storing tokens to be in order to check for replay attacks 
            /*
            try {
                this.jdbcTemplate.update(INSERT, token.replayPreventionToken);
            } catch (DataAccessException e) {
                logger.error(e);
                throw new AuthenticationServiceException("Unable to authenticate. The replay attack prevention " +
                        "token already exists, so this is probably a replay attack.");
            }
            */

            UserDetails user = registerAAFUser(token.attributes);

            return new AAFAuthentication(user, token.attributes, token, true);
        } catch (IOException e) {
            log.error("ERROR:  " + e.getMessage());
            throw new AuthenticationServiceException(e.getLocalizedMessage());
        }
    }

    private UserDetails registerAAFUser(AAFAttributes attributes){
        BuiltInUserAuthenticationService uds = new BuiltInUserAuthenticationService();
        this.allocatorEntiryService = new AllocatorEntityService();
        this.prefixEntityService = new PrefixEntityService ();
        this.registrantEntityService = new RegistrantEntityService(this.prefixEntityService, this.allocatorEntiryService);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_REGISTRANT");
        Registrant r = this.registrantEntityService.searchRegistrant(attributes.email);
        UserDetails userDetails = null;
        if(r != null){
            if(r.getIsactive()) {
                log.info("logging in as AAF User: " + attributes.email);
                userDetails = new User(r.getUsername(), r.getPassword(), Arrays.asList(authority));
            }
            else{
                throw new AuthenticationServiceException("Unable to authenticate");
            }
        }
        else{
            log.info("USER DOESN'T EXIST ATTEMPTING TO ADD:  " + attributes.email);
            String passwordStr = "AAF Authenticated";
            try {
                this.registrantEntityService.addRegistrant(Config.get("AAF_DEFAULT_ALLOCATOR"),
                        attributes.email,
                        attributes.displayName,
                        attributes.email,
                        passwordStr);
                this.registrantEntityService.allocatePrefix(Config.get("AAF_DEFAULT_PREFIX"), attributes.email);
                userDetails = new User(attributes.email, passwordStr, Arrays.asList(authority));
            } catch (Exception e) {
                log.error("ERROR:  " + e.getMessage());
                e.printStackTrace();
            }

        }

        return userDetails;
    }
}