package org.csiro.igsn.security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.csiro.igsn.entity.postgres.Allocator;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.AllocatorEntityService;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;


public class RegistryUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean isAllocator;
    private ArrayList<String> roles;

    public  RegistryUser(UserDetails details) {
        this.username = details.getUsername();
        this.roles = new ArrayList<String>();
        RegistrantEntityService registrantEntityService = new RegistrantEntityService(null, null);
        AllocatorEntityService allocatorService = new AllocatorEntityService();
        Registrant r = registrantEntityService.searchRegistrant(this.username);
        if(r != null){
            this.name = r.getRegistrantname();
            this.email = r.getRegistrantemail();
            this.addRole("ROLE_REGISTRANT");
            if(allocatorService.searchAllocator(this.username)!=null){
                this.setAllocator(true);
                this.addRole("ROLE_ALLOCATOR");
            }else{
                this.setAllocator(false);
            }
        }else{
            Allocator a = allocatorService.searchAllocator(this.username);
            this.name = a.getContactname();
            this.email = a.getContactemail();
            this.addRole("ROLE_ALLOCATOR");
            this.setAllocator(true);
        }
    }

    public  RegistryUser(String userName) {
        this.username = userName;
        this.roles = new ArrayList<String>();
        RegistrantEntityService registrantEntityService = new RegistrantEntityService(null, null);
        AllocatorEntityService allocatorService = new AllocatorEntityService();
        Registrant r = registrantEntityService.searchRegistrant(this.username);
        if(r != null){
            this.name = r.getRegistrantname();
            this.email = r.getRegistrantemail();
            this.password = r.getPassword();
            this.addRole("ROLE_REGISTRANT");
            if(allocatorService.searchAllocator(this.username)!=null){
                this.setAllocator(true);
                this.addRole("ROLE_ALLOCATOR");
            }else{
                this.setAllocator(false);
            }
        }else{
            Allocator a = allocatorService.searchAllocator(this.username);
            this.name = a.getContactname();
            this.email = a.getContactemail();
            this.password = a.getPassword();
            this.addRole("ROLE_ALLOCATOR");
            this.setAllocator(true);
        }
    }



    public RegistryUser() {
        this.name = "";
        this.email = "";
        this.username = "";
        this.roles = new ArrayList<String>();

    }

    public boolean isEnabled() {
        return true;
    }

    public void setName(String name){
        this.name = name;

    }

    public String getName(){
        return this.name;
    }

    public String getUserName(){

        return this.username;
    }

    public String getEmail(){
        return this.email;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (String role : this.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        } return grantedAuthorities;

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getRoles(){
        return this.roles;
    }

    public void addRole(String aRole) {
        if(!this.roles.contains(aRole))
            this.roles.add(aRole);
    }

    public String getRole(String aRole) {
        if(this.roles.contains(aRole))
            return aRole;
        else
            return "";
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;

        AllocatorEntityService allocatorService = new AllocatorEntityService();
        if(allocatorService.searchAllocator(this.username)!=null){
            this.setAllocator(true);
        }else{
            this.setAllocator(false);
        }
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }


    public boolean isAllocator() {
        return isAllocator;
    }

    public void setAllocator(boolean isAllocator) {
        this.isAllocator = isAllocator;
    }

}