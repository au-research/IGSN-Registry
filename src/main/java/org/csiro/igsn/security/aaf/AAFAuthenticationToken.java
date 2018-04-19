package org.csiro.igsn.security.aaf;

import org.apache.log4j.Logger;
import org.csiro.igsn.security.BuiltInUserAuthenticationService;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by wis056 on 8/04/2015.
 */
public class AAFAuthenticationToken  extends AbstractAuthenticationToken {
    
    private static final long serialVersionUID = -1118229819090274846L;
    final Logger log = Logger.getLogger(AAFAuthenticationToken.class);
    private String credentials;

    public AAFAuthenticationToken(String token) {
        super(null);
        log.debug("token:" + token);
        this.credentials = token;
    }

    public String getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return "";
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}
