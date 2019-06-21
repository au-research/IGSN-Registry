package org.csiro.igsn.security;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.Allocator;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.AllocatorEntityService;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

public class BuiltInUserAuthenticationService implements UserDetailsService {
    final Logger log = Logger.getLogger(BuiltInUserAuthenticationService.class);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RegistrantEntityService registrantEntityService = new RegistrantEntityService(null, null);
        Registrant r = registrantEntityService.searchRegistrant(username);

        if (r != null) {
            log.info("Logging in Registrant: " + username);
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_REGISTRANT");
            UserDetails userDetails = new User(r.getUsername(),
                    r.getPassword(), Arrays.asList(authority));

            return userDetails;
        }

        AllocatorEntityService allocatorEntityService = new AllocatorEntityService();
        Allocator a = allocatorEntityService.searchAllocator(username);

        if (a != null) {
            log.info("Logging in Allocator: " + username);
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ALLOCATOR");
            UserDetails userDetails = new User(a.getUsername(),
                    a.getPassword(), Arrays.asList(authority));
            return userDetails;
        }
        else{
            log.info("No built in user by the name: " + username);
            return null;
        }
    }
}
