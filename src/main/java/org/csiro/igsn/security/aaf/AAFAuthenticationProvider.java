package org.csiro.igsn.security.aaf;

import org.apache.log4j.Logger;
import org.csiro.igsn.security.BuiltInUserAuthenticationService;
import org.csiro.igsn.security.aaf.JWTManagement;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by wis056 on 8/04/2015.
 */
public class AAFAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private JWTManagement jwtManagement;
    final Logger log = Logger.getLogger(AAFAuthenticationProvider.class);

    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.jwtManagement, "A JWT management system must be set");
        Assert.notNull(this.messages, "A message source must be set");
        doAfterPropertiesSet();
    }

    protected void doAfterPropertiesSet() {
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setJwtManagement(JWTManagement jwtManagement) {
        this.jwtManagement = jwtManagement;
    }
    
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Authentication a = null;

        try {
            Assert.isInstanceOf(AAFAuthenticationToken.class, authentication, messages.getMessage(
                    "AAFAuthenticationProvider.onlySupports",
                    "Only AAFAuthenticationToken is supported"));
            a = this.jwtManagement.parseJWT(((AAFAuthenticationToken) authentication).getCredentials());
        }
        catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error( errors);
            e.printStackTrace();
        }
        return a;
    }

    public boolean supports(Class<?> authentication) {
        return (AAFAuthenticationToken.class.isAssignableFrom(authentication));
    }
    
}
