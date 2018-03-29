package org.csiro.igsn.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.csiro.igsn.security.AuthenticationDetailsSource.AuthenticationDetails;

public class RegistryUserAuthenticationProvider  implements AuthenticationProvider {
    final Logger log = Logger.getLogger( RegistryUserAuthenticationService.class);
        @Override
        public Authentication authenticate(Authentication authentication)
                throws AuthenticationException {

            // cast as it pass the support method
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            String principal = (String) auth.getPrincipal();
            String credential = (String) auth.getCredentials();

            Object obj = auth.getDetails();
            if (obj instanceof AuthenticationDetails) {
                AuthenticationDetails details = (AuthenticationDetails) obj;
                // Doing the extra check such as referer
                log.info("details.getReferer" + details.getReferer());
            }
                    // do the further check ...
                    // return the result if passed validation
                    UsernamePasswordAuthenticationToken result =
                            new UsernamePasswordAuthenticationToken(principal, credential);
                    return result;
        }





        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);

        }
}
