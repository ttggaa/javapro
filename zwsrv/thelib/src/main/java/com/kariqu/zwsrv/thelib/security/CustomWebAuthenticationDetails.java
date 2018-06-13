package com.kariqu.zwsrv.thelib.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by simon on 8/12/16.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    LinkedHashMap<String,String> parameterMap = new LinkedHashMap<String,String>();

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        Map<String, String[]> originalParameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : originalParameterMap.entrySet()) {
            if (entry.getValue().length>0) {
                parameterMap.put(entry.getKey(),entry.getValue()[0]);
            }
        }
    }

    public LinkedHashMap<String, String> getParameterMap() {
        return parameterMap;
    }
}
