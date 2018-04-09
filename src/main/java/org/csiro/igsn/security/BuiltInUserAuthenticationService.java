package org.csiro.igsn.security;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.autheticators.BuiltInUserDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

public class BuiltInUserAuthenticationService implements UserDetailsService {
    final Logger log = Logger.getLogger(BuiltInUserAuthenticationService.class);
    private BuiltInUserDAO builtInUserDAO = new BuiltInUserDAO();


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Built In UserName:" + username);
        RegistryUser allocatorUserInfo = builtInUserDAO.getAllocator(username);
        if(allocatorUserInfo != null){
            GrantedAuthority authority = new SimpleGrantedAuthority(allocatorUserInfo.getRole("ROLE_ALLOCATOR"));
            UserDetails userDetails = new User(allocatorUserInfo.getUsername(),
                    allocatorUserInfo.getPassword(), Arrays.asList(authority));
            return userDetails;
        }
        else {
            RegistryUser registrantUserInfo = builtInUserDAO.getRegistrant(username);
            GrantedAuthority authority = new SimpleGrantedAuthority(registrantUserInfo.getRole("ROLE_REGISTRANT"));
            UserDetails userDetails = new User(registrantUserInfo.getUsername(),
                    registrantUserInfo.getPassword(), Arrays.asList(authority));
            return userDetails;
        }

    }
}
