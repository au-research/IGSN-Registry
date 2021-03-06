package org.csiro.igsn.security;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.csiro.igsn.security.aaf.AAFAuthenticationFilter;
import org.csiro.igsn.security.aaf.AAFAuthenticationProvider;
import org.csiro.igsn.security.aaf.JWTManagement;
import org.csiro.igsn.utilities.Config;
import org.csiro.igsn.utilities.SharedSecretEcoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.csiro.igsn.utilities.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.authentication.SpringSecurityAuthenticationSource;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
@Configuration
@EnableWebSecurity
public class MultiHttpSecurityConfig {


    @Configuration
    @Order(1)
    public static class AAFSecurityConfig extends
            WebSecurityConfigurerAdapter {

    	static final String JWTPATH = "/auth/jwt";

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher(JWTPATH)
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest().hasAnyRole("ADMIN", "REGISTRANT", "ALLOCATOR")
                    .and()
                    .addFilterBefore(
                            getAAFauthenticationFilter(),
                            UsernamePasswordAuthenticationFilter.class)
                    .logout()
                    .logoutUrl("/logout");
        }

        @Bean
        public UserDetailsService registrantUserDetailsService() {
            return new RegistrantAuthenticationService();
        };


        @Autowired
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            SharedSecretEcoder encoder = new SharedSecretEcoder();
            AAFAuthenticationProvider ap = new AAFAuthenticationProvider();
            ap.setJwtManagement(new JWTManagement());
            auth.authenticationProvider(ap);
            auth.userDetailsService(registrantUserDetailsService()).passwordEncoder(encoder);
        }

        @Bean
        public AAFAuthenticationFilter
        getAAFauthenticationFilter() throws Exception {
            AAFAuthenticationFilter authenticationFilter
                    = new AAFAuthenticationFilter();
            authenticationFilter.setAuthenticationSuccessHandler(new CustomSuccessHandler());
            authenticationFilter.setAuthenticationFailureHandler(new CustomFailureHandler());
            authenticationFilter.setRequiresAuthenticationRequestMatcher(
                    new AntPathRequestMatcher(JWTPATH, "POST"));
            authenticationFilter.setAuthenticationManager(authenticationManagerBean());
            return authenticationFilter;
        }

        private class CustomSuccessHandler implements AuthenticationSuccessHandler{
            private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                Authentication authentication)
                    throws IOException, ServletException {
                HttpSession session = httpServletRequest.getSession();
                UserDetails authUser = (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                session.setAttribute("username", authUser.getUsername());
                session.setAttribute("name", authUser.getUsername());
                session.setAttribute("authorities", authentication.getAuthorities());
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/#/addresource");
            }
        }

		private class CustomFailureHandler implements AuthenticationFailureHandler {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request,
												HttpServletResponse response, AuthenticationException ae)
					throws IOException, ServletException {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.writeValue(response.getWriter(), "Unauthorised!");
			}
		}
    }

		@Configuration
		@Order(3)
		public static class APISecurityConfig extends
				WebSecurityConfigurerAdapter {

			@Override
			protected void configure(HttpSecurity http) throws Exception {

				http.antMatcher("/api/**").httpBasic()
						.and().authorizeRequests()
						.antMatchers("/api/subnamespace/**").authenticated()
						.antMatchers("/api/metadata/**").authenticated()
						.antMatchers("/api/igsn/**").authenticated()
						.and().csrf().disable();

			}

			@Bean
			public UserDetailsService builtInUserUserDetailsService() {
				return new BuiltInUserAuthenticationService();
			}

			@Autowired
			public void configure(AuthenticationManagerBuilder auth) throws Exception {
				ShaPasswordEncoder encoder = new ShaPasswordEncoder();
				auth.userDetailsService(builtInUserUserDetailsService()).passwordEncoder(encoder);
			}


//			@Autowired
//			public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//				auth.ldapAuthentication()
//						.userDetailsContextMapper(new UserDetailsContextMapperImpl())
//						.userSearchFilter("(&(sAMAccountName={0}))")
//						.groupRoleAttribute("cn").groupSearchBase("ou=Groups").groupSearchFilter("(&(member={0}))")
//						.contextSource(getLdapContextSource());
//
//			}

//			private static LdapContextSource getLdapContextSource() throws Exception {
//				LdapContextSource cs = new LdapContextSource();
//				cs.setUrl(Config.getLdapUrl());
//				cs.setBase(Config.getLDAPBase());
//				cs.setUserDn(Config.getUserDN());
//				//cs.setAnonymousReadOnly(true);
//				Hashtable<String, Object> env = new Hashtable<String, Object>();
//				env.put(Context.REFERRAL, "follow");
//				cs.setBaseEnvironmentProperties(env);
//				cs.setPassword(Config.getLdapPassword());
//				cs.afterPropertiesSet();
//				return cs;
//			}




		}

	@Configuration
	@Order(4)
	public static  class BuiltInSecurityConfig extends
			WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
                    .antMatchers("/restricted/registrant.html").hasAnyRole("ALLOCATOR", "ADMIN")
					.antMatchers("/restricted/addResource.html").hasAnyRole("ALLOCATOR", "REGISTRANT")
					.antMatchers("/web/**").authenticated()
					.and()
					.formLogin()
					.usernameParameter("j_username") // default is username
					.passwordParameter("j_password") // default is password
					.loginPage("/views/login.html").successHandler(new CustomSuccessHandler()).failureUrl("/views/login.html?failure")
					.and()
					.logout().logoutSuccessUrl("/")
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.and()
					.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
					.csrf().csrfTokenRepository(csrfTokenRepository());
		}

		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			repository.setHeaderName("X-XSRF-TOKEN");
			return repository;
		}

		@Bean
		public UserDetailsService builtInUserUserDetailsService() {
		    return new BuiltInUserAuthenticationService();
		};

		@Autowired
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			ShaPasswordEncoder encoder = new ShaPasswordEncoder();
			auth.userDetailsService(builtInUserUserDetailsService()).passwordEncoder(encoder);
		}

		protected class CustomSuccessHandler implements AuthenticationSuccessHandler{

			@Override
			public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
												HttpServletResponse httpServletResponse,
												Authentication authentication)
					throws IOException, ServletException {

				HttpSession session = httpServletRequest.getSession();
				UserDetails authUser = (UserDetails) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				session.setAttribute("username", authUser.getUsername());
				session.setAttribute("name", authUser.getUsername());
				session.setAttribute("authorities", authentication.getAuthorities());
                RegistryUser r = new RegistryUser(authUser.getUsername());
                r.setPassword("HIDDEN");
				// set our response to OK status
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
				httpServletResponse.setContentType("text/html; charset=UTF-8");
				Gson gson = new Gson();
				httpServletResponse.getWriter().write(gson.toJson(r));
			}
		}
	}

}
