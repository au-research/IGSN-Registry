package org.csiro.igsn.security;
import java.util.ArrayList;
import java.util.Collection;

import org.csiro.igsn.entity.service.AllocatorEntityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


public class RegistryUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    private UserDetails details;
    private String name;
    private String userName;
    private String email;
    private String password;
    private boolean isAllocator;
    private ArrayList<String> roles;

    public RegistryUser() {
        this.details = null;
        this.name = "";
        this.email = "";
        this.userName = "";
        this.roles = new ArrayList<String>();
    }

    public boolean isEnabled() {
        return details.isEnabled();
    }

    public void setName(String name){
        this.name = name;

    }

    public String getName(){
        return this.name;
    }

    public String getUserName(){

        return this.userName;
    }

    public String getEmail(){
        return this.email;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.details.getAuthorities();
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
        this.userName = username;

        AllocatorEntityService allocatorService = new AllocatorEntityService();
        if(allocatorService.searchAllocator(this.userName)!=null){
            this.setAllocator(true);
        }else{
            this.setAllocator(false);
        }
    }

    public String getUsername() {
        return this.userName;
    }

    public boolean isAccountNonExpired() {
        //return details.isAccountNonExpired();
        return true;
    }

    public boolean isAccountNonLocked() {
        //return details.isAccountNonLocked();
        return true;
    }

    public boolean isCredentialsNonExpired() {
        // return details.isCredentialsNonExpired();
        return true;
    }

    public boolean isAllocator() {
        return isAllocator;
    }

    public void setAllocator(boolean isAllocator) {
        this.isAllocator = isAllocator;
    }

}