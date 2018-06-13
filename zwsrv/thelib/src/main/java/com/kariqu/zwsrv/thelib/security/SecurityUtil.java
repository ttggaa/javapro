package com.kariqu.zwsrv.thelib.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by simon on 11/04/17.
 */
public class SecurityUtil {

    public static CurrentUserDetails currentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication!=null) {
            if (authentication.getPrincipal() instanceof CurrentUserDetails) {
                CurrentUserDetails currentUser = (CurrentUserDetails)authentication.getPrincipal();
                return currentUser;
            }
        }
        return null;
    }

    public static int currentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof CurrentUserDetails) {
                CurrentUserDetails currentUser = (CurrentUserDetails)authentication.getPrincipal();
                return currentUser.getUserId();
            }
        }
        return 0;
    }
}
