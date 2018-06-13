package com.kariqu.zwsrv.thelib.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * Created by simon on 11/04/17.
 */
@Component
public class AccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * A Spring Security UserDetailsService implementation based upon the
     * Account entity model.
     */
    @Autowired
    CustomUserDetailsService userDetailsService;

    /**
     * A PasswordEncoder instance to hash clear test password values.
     */
    @Autowired
    PasswordEncoder passwordEncoderBean;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        logger.debug("> additionalAuthenticationChecks");
        if (usernamePasswordAuthenticationToken.getCredentials() == null || userDetails.getPassword() == null) {
            throw new BadCredentialsException("Credentials may not be null.");
        }

        if (!passwordEncoderBean.matches((String) usernamePasswordAuthenticationToken.getCredentials(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials.");
        }
        logger.debug("< additionalAuthenticationChecks");
    }

    @Override
    protected UserDetails retrieveUser(String identifier, UsernamePasswordAuthenticationToken authenticationToken) throws AuthenticationException {

        UserDetails userDetails = null;
        //refer to AccountAuthenticationProvider::authenticate(Authentication authentication)
        if (authenticationToken.getPrincipal()!=null&&authenticationToken.getPrincipal() instanceof CurrentUserDetails) {
            userDetails = (CurrentUserDetails)authenticationToken.getPrincipal();
        }
        else {
            Object details = authenticationToken.getDetails();
            if (details!=null) {
                if (details instanceof LinkedHashMap) {
                    LinkedHashMap<String,String> detailsMap = (LinkedHashMap<String,String>)authenticationToken.getDetails();

                    String appId = detailsMap.get("appid");
                    String authType = detailsMap.get("authtype");

                    userDetails = userDetailsService.loadUserByIdentifier(appId,authType,identifier);
                }
                else if (details instanceof CustomWebAuthenticationDetails) {
                    LinkedHashMap<String,String> detailsMap = ((CustomWebAuthenticationDetails) details).getParameterMap();
                    if (detailsMap!=null) {
                        String appId = detailsMap.get("appid");
                        String authType = detailsMap.get("authtype");

                        userDetails = userDetailsService.loadUserByIdentifier(appId,authType,identifier);
                    }
                }
            }
        }

        if (userDetails==null) {
            userDetails = new CurrentUserDetails();
//            throw new BadCredentialsException("Invalid credentials.");
        }
        return userDetails;
    }
}
