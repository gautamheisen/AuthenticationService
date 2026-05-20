package com.gautam.authenticationservice.security.models;

import com.gautam.authenticationservice.models.Role;
import com.gautam.authenticationservice.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<CustomGrantedAuthority> customGrantedAuthorities = new ArrayList<>();

        for (Role role: user.getRoles()) {
            customGrantedAuthorities.add(new CustomGrantedAuthority(role));
        }

        return customGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // if (Date.now() - lastPasswordUpdateDate > 90) {return false} return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
