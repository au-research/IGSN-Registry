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
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean tcAccepted;



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
            this.tcAccepted = r.getTcAccepted();
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
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
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
            this.tcAccepted = r.getTcAccepted();
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
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }



    public RegistryUser() {
        this.name = "";
        this.email = "";
        this.username = "";
        this.roles = new ArrayList<String>();
        this.enabled = false;
        this.accountNonExpired = false;
        this.accountNonLocked = false;
        this.credentialsNonExpired = false;
        this.tcAccepted = false;

    }

    public boolean isEnabled() {
        return this.enabled;
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

    public Boolean getTcAccepted(){
        return this.tcAccepted;
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

    public void setTC(Boolean tc_accepted) {
        this.tcAccepted = tc_accepted;
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
        return this.accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }


    public boolean isAllocator() {
        return isAllocator;
    }

    public void setAllocator(boolean isAllocator) {
        this.isAllocator = isAllocator;
    }

}