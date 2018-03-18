package org.csiro.igsn.security;
import java.util.Collection;

import org.csiro.igsn.entity.service.AllocatorEntityService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

  public class LdapUser implements LdapUserDetails {

     private static final long serialVersionUID = 1L;

     private LdapUserDetails details;     
     private String name;
     private String userName;
     private String email;   
     private boolean isAllocator;

     public LdapUser(LdapUserDetails details, String name, String email) {
         this.details = details;
         this.name = name;
         this.email = email;
         this.userName = details.getUsername();
         AllocatorEntityService allocatorService = new AllocatorEntityService();
         if(allocatorService.searchAllocator(this.userName)!=null){
        	 this.setAllocator(true);
         }else{
        	 this.setAllocator(false);
         }
         
     }

     public boolean isEnabled() {
         return details.isEnabled();
     }

     public String getName(){
    	 return name;
     }
     
     public String getUserName(){
    	 return userName;
     }
     
     public String getEmail(){
    	 return email;
     }

     public String getDn() {
         return details.getDn();
     }

     public Collection<? extends GrantedAuthority> getAuthorities() {
         return details.getAuthorities();
     }

     public String getPassword() {
         return details.getPassword();
     }

     public String getUsername() {
         return details.getUsername();
     }

     public boolean isAccountNonExpired() {
         return details.isAccountNonExpired();
     }

     public boolean isAccountNonLocked() {
         return details.isAccountNonLocked();
     }

     public boolean isCredentialsNonExpired() {
         return details.isCredentialsNonExpired();
     }

	public boolean isAllocator() {
		return isAllocator;
	}

	public void setAllocator(boolean isAllocator) {
		this.isAllocator = isAllocator;
	}
  
  }