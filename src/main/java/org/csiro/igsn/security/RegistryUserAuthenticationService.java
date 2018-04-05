package org.csiro.igsn.security;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.csiro.igsn.entity.mysql.UserDAO;
import org.csiro.igsn.web.controllers.WebFormIGSNMintCtrl;
import org.apache.log4j.Logger;

@Service
public class  RegistryUserAuthenticationService implements UserDetailsService {

    final Logger log = Logger.getLogger( RegistryUserAuthenticationService.class);
    private  UserDAO userDAO = new UserDAO();
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("USERNAME BEFORE" + username);
        RegistryUser userInfo = userDAO.getUserInfo(username);
        log.info("USERINFO USERNAME" + userInfo.getUsername());
        GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole("ROLE_ADMIN"));
        UserDetails userDetails = new User(userInfo.getUsername(),
                userInfo.getPassword(), Arrays.asList(authority));
        return userDetails;
    }
}
