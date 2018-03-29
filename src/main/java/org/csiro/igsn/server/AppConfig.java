package org.csiro.igsn.server;

import org.csiro.igsn.security.MultiHttpSecurityConfig;
import org.csiro.igsn.security.RegistryUserAuthenticationProvider;
import org.csiro.igsn.security.RegistryUserAuthenticationService;
import org.csiro.igsn.utilities.ShaPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


@ImportResource(value = {"/WEB-INF/applicationContext.xml"})
@Configuration
public class AppConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(AuthenticationManagerBuilder builder)
            throws Exception {
        builder.authenticationProvider(new RegistryUserAuthenticationProvider());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new RegistryUserAuthenticationService();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder();
        auth.userDetailsService(userDetailsService()).passwordEncoder(encoder);
    }

//	 /**
//	  * Useless unless remove  <mvc:annotation-driven /> 
//      * @return MarshallingHttpMessageConverter object which is responsible for
//      *         marshalling and unMarshalling process
//      */
//    @Bean(name = "marshallingHttpMessageConverter")
//    public MarshallingHttpMessageConverter getMarshallingHttpMessageConverter() {
// 
//    	  Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//    	  jaxb2Marshaller.setPackagesToScan("org.csiro.igsn.bindings.*");
//    	  Map<String,Object> map = new HashMap<String,Object>();
//    	  map.put("jaxb.formatted.output", true);
//    	  map.put(Marshaller.JAXB_SCHEMA_LOCATION, "http://igsn.org/schema/kernel-v.1.0 https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd");
//    	  jaxb2Marshaller.setMarshallerProperties(map);
//    	  try{
//    		  jaxb2Marshaller.setSchema(new UrlResource("https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd"));
//    	  }catch(MalformedURLException e){
//    		  e.printStackTrace();
//    		  return null;
//    	  }
//    	  
//    	
//        MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
//        marshallingHttpMessageConverter.setMarshaller(jaxb2Marshaller);
//        marshallingHttpMessageConverter.setUnmarshaller(jaxb2Marshaller);
//        return marshallingHttpMessageConverter;
//    }
 
   
	
   
//	public Jaxb2Marshaller getJaxb2Marshaller() {
//	  Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//	  jaxb2Marshaller.setPackagesToScan("org.csiro.igsn.bindings.*");
//	  Map<String,Object> map = new HashMap<String,Object>();
//	  map.put("jaxb.formatted.output", true);
//	  map.put(Marshaller.JAXB_SCHEMA_LOCATION, "http://igsn.org/schema/kernel-v.1.0 https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd");
//	  jaxb2Marshaller.setMarshallerProperties(map);
//	  try{
//		  jaxb2Marshaller.setSchema(new UrlResource("https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd"));
//	  }catch(MalformedURLException e){
//		  e.printStackTrace();
//		  return null;
//	  }
//      return jaxb2Marshaller;
//	}
}
