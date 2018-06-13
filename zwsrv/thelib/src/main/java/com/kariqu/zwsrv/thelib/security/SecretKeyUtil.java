package com.kariqu.zwsrv.thelib.security;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 25/01/2018.
 */
public class SecretKeyUtil {

    public static final Map<String,String> apiSecretKeyMap = new HashMap<String,String>() {
        {
            put("android-zwsrv","e191be1760af42c9");
            put("ios","83bbebc4e78849ba");
            put("android","6ce83ddf7eb144a2");
        }
    };

    public static final String apiSecretKey(HttpServletRequest httpServletRequest) {
        return apiSecretKey(httpServletRequest.getHeader(SecurityConstants.HTTP_HEADER_KEY_DEVICE_ID),
                httpServletRequest.getHeader(SecurityConstants.HTTP_HEADER_KEY_OS));
    }

    public static final String apiSecretKey(String deviceId, String osString) {
        String secretKeyReturn = "";
        if (osString!=null&&osString.length()>0) {
            String secretKey = apiSecretKeyMap.get(osString.toLowerCase());
            if (secretKey!=null&&secretKey.length()>8) {
                if (deviceId!=null&&deviceId.length()>8) {
                    secretKeyReturn += deviceId.substring(0,8);
                }
                if (secretKeyReturn.length()==8) {
                    secretKeyReturn += secretKey.substring(8,secretKey.length()-8);
                } else {
                    secretKeyReturn = secretKey;
                }
            }
        }
        return secretKeyReturn;
    }
}
