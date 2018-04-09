package org.csiro.igsn.security;

import java.util.Arrays;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.csiro.igsn.entity.autheticators.RegistryUserDAO;
import org.apache.log4j.Logger;

@Service
public class  RegistryUserAuthenticationService implements UserDetailsService {

    final Logger log = Logger.getLogger( RegistryUserAuthenticationService.class);
    private RegistryUserDAO userDAO = new RegistryUserDAO();
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Registry UserName:" + username);
        RegistryUser userInfo = userDAO.getUserInfo(username);
        GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole("ROLE_ADMIN"));
        UserDetails userDetails = new User(userInfo.getUsername(),
                userInfo.getPassword(), Arrays.asList(authority));
        return userDetails;
    }
}
