package com.kariqu.zwsrv.thelib.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by simon on 11/04/17.
 */
public class CurrentUserDetails implements UserDetails {

    String appId;
    int    authId;
    String authType;
    String identifier;
    String credential;

    int userId;
    String nickName;
    int role;
    int gender;

    public CurrentUserDetails(JwtAuthenticationClaims claims) {
        appId=claims.getAppId();
        authId=claims.getAuthId();

        authType=claims.getAuthType();
        identifier=claims.getIdentifier();

        userId=claims.getUserId();
        nickName=claims.getNickName();
        gender=claims.getGender();

        setRole(claims.getRole());
    }

    public CurrentUserDetails() {

    }

    Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
        this.authorities.clear();
        List<String> roles = SecurityConstants.roleStringList(role);
        for (String tmp : roles) {
            this.authorities.add(new SimpleGrantedAuthority(tmp));
        }
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return getCredential();
    }

    @Override
    public String getUsername() {
        return getIdentifier();
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
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
