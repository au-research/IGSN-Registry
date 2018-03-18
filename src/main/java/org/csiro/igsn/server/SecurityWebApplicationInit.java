package org.csiro.igsn.server;

import org.csiro.igsn.security.MultiHttpSecurityConfig;
import org.csiro.igsn.security.UserDetailsContextMapperImpl;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInit
      extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInit() {
        super(MultiHttpSecurityConfig.class);
    }
}