package com.kariqu.zwsrv.thelib.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by simon on 7/14/16.
 */
//https://stackoverflow.com/questions/28057592/spring-boot-accessdeniedhandler-does-not-work
//The AccessDeniedHandler only applies to authenticated users. The default behaviour for unauthenticated users is to redirect to the login page (or whatever is appropriate for the authentication mechanism in use).
//
//        If you want to change that you need to configure an AuthenticationEntryPoint, which is invoked when an unauthenticated user attempts to access a protected resource. You should be able to use
//
//        http.exceptionHandling().authenticationEntryPoint(...)

public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String jsonString = convertObject2Json(new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED));
        out.print(jsonString);
    }

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObject2Json(Object o) {
        if(o == null){
            return null;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
