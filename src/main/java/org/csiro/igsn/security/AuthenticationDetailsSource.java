package org.csiro.igsn.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticationDetailsSource  extends
            WebAuthenticationDetailsSource {
    final Logger log = Logger.getLogger( AuthenticationDetailsSource.class);
        @Override
        public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
            return new AuthenticationDetails(context);
        }

        @SuppressWarnings("serial")
        class AuthenticationDetails extends WebAuthenticationDetails {

            private final String referer;

            public AuthenticationDetails(HttpServletRequest request) {
                super(request);
                this.referer = request.getHeader("Referer");
                log.info("REFERER" + this.referer);
            }

            public String getReferer() {
                return referer;
            }
        }
}



