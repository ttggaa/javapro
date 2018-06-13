package com.kariqu.zwsrv.thelib.security;

import com.kariqu.zwsrv.thelib.persistance.entity.Auth;
import com.kariqu.zwsrv.thelib.persistance.entity.User;
import com.kariqu.zwsrv.thelib.persistance.service.AuthService;
import com.kariqu.zwsrv.thelib.persistance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 11/04/17.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoderBean;

    public UserDetails loadUserByIdentifier(String appId, String authType, String identifier) throws UsernameNotFoundException {

        Auth auth = authService.findByAuthTypeAndIdentifier(appId,authType,identifier);
        if (auth!=null) {

            User user = userService.findOne(auth.getUserId());
            if (user!=null) {

                CurrentUserDetails userDetails = new CurrentUserDetails();
                userDetails.setAppId(appId);
                userDetails.setAuthType(authType);
                userDetails.setIdentifier(identifier);
                userDetails.setCredential(auth.getCredential());
                userDetails.setAuthId(auth.getAuthId());
                userDetails.setUserId(auth.getUserId());
                userDetails.setNickName(user.getNickName());
                userDetails.setRole(user.getRole());
                return userDetails;
            }
        }
        throw new UsernameNotFoundException("Not Exist User");
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Not Exist User");
    }
}
