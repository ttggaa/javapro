package com.kariqu.zwsrv.thelib.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.TokenExpiredException;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.util.AESCoder;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
//import com.haozhushou.security.CustomUserDetailsService;

/**
 * Created by simon on 11/04/17.
 */
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    protected final transient Logger logger = LoggerFactory.getLogger("TokenFilter");

    public static final String apiSecretKeySign = "ToKEnpwd^*8L2017";

    @Autowired
    CustomUserDetailsService service;

    @Autowired
    JwtAuthenticationTokenProvider tokenProvider;

    boolean isOnline = false;
    List<String> ignoreFilterList = new ArrayList<>();

    public JwtAuthenticationTokenFilter() {
        this(null,false);
    }

    public JwtAuthenticationTokenFilter(List<String> ignoreFilterList, boolean isOnline) {
        this.isOnline=isOnline;
        if (ignoreFilterList!=null) {
            this.ignoreFilterList.addAll(ignoreFilterList);
        }
    }


    public static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            System.out.println(key + "：" + value);
            map.put(key, value);
        }
        return map;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        try {
            if (!(request instanceof HttpServletRequest)) {
                throw new RuntimeException("Expecting an HTTP request");
            }

            HttpServletRequest httpServletRequest = (HttpServletRequest)request;

            boolean filtered = false;
            String requestURI = httpServletRequest.getRequestURI();
            //logger.info("JwtAuthenticationTokenFilter:{} ",requestURI!=null?requestURI:"");
            for (String prefix : ignoreFilterList) {
                if (requestURI.startsWith(prefix)) {
                    filtered=true;
                }
            }

            if (!filtered) {
                if ("POST".equals(httpServletRequest.getMethod().toUpperCase())) {
                    boolean handled = false;
                    if (httpServletRequest instanceof ServletRequestWrapper) {
                        ServletRequestWrapper servletRequestWrapper = (ServletRequestWrapper)httpServletRequest;
                        while (true) {
                            if (servletRequestWrapper instanceof BodyReaderHttpServletRequestWrapper) {
                                handled=true;
                                break;
                            }
                            if (servletRequestWrapper.getRequest() instanceof ServletRequestWrapper) {
                                servletRequestWrapper=(ServletRequestWrapper)servletRequestWrapper.getRequest();
                            } else {
                                break;
                            }
                        }
                    }

                    if (!handled) {
                        String secretKey = "";
                        if (isOnline) {
                            secretKey = SecretKeyUtil.apiSecretKey(httpServletRequest);
                            if (TextUtils.isEmpty(secretKey)) {
                                //线上必须是加密的post请求
                                output((HttpServletResponse) response, buildErrorEntryJsonString(ErrorCode.ERROR_ACCESS_DENIED,null));
                                return;
                            }
                        }
                        httpServletRequest = new BodyReaderHttpServletRequestWrapper((HttpServletRequest)request,secretKey);
                    }
                }
                else {
                    if (isOnline) {
                        //线上必须是加密的post请求
                        output((HttpServletResponse) response, buildErrorEntryJsonString(ErrorCode.ERROR_ACCESS_DENIED,null));
                        return;
                    }
                }
            }

            /*
            * //签名:AES(uid|timestamp)
            * NSString *encryptKey = @"SignPWd!2^*8AiBa";
            * NSString *signString= [[NSString stringWithFormat:@"%ld|%@",[Client sharedInstance].currentUserId,[NSNumber numberWithLongLong:timestamp].stringValue] AES128EncryptWithKey:encryptKey];
            * [dict setObject:signString forKey:@"sign"];
            * */
            if (!filtered) {
                boolean isValid=false;
                String signString= httpServletRequest.getHeader("sign");
                if (signString!=null) {
                    String decryptedValue= AESCoder.decrypt(apiSecretKeySign.getBytes(), signString);
                    if (decryptedValue!=null) {
                        StringTokenizer tokenizer=new StringTokenizer(decryptedValue,"|");
                        if (tokenizer.countTokens()>=3) {
                            String timestampString=tokenizer.nextToken();
                            String timeIntevalDifferenceString=tokenizer.nextToken();

                            if (NumberValidationUtil.isWholeNumber(timestampString)
                                    &&NumberValidationUtil.isWholeNumber(timeIntevalDifferenceString)) {
                                long timestamp= Long.valueOf(timestampString);
                                long timeIntevalDifference= Long.valueOf(timeIntevalDifferenceString);
                                if (timeIntevalDifference!=0) {
                                    long localTime = System.currentTimeMillis();
                                    //2分钟不能访问
                                    if (Math.abs(localTime-timestamp)>2*60*1000) {
                                        //不允许访问
                                        //...
                                    } else {
                                        isValid=true;
                                    }
                                } else {
                                    //小概率...
                                    isValid=true;
                                }
                            }
                        }
                    }
                }
                if (!isValid) {
//                    output((HttpServletResponse) response, buildErrorEntryJsonString(ErrorCode.ERROR_ACCESS_DENIED));
//                    return;
                }
            }

            String token = httpServletRequest.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
            if (!StringUtils.hasText(token)) {
                Cookie[] cookies = httpServletRequest.getCookies();
                if (cookies!=null
                        &&cookies.length>0) {
                    for (Cookie cookie:cookies) {
                        if (cookie.getName()!=null
                                &&cookie.getName().equalsIgnoreCase(SecurityConstants.AUTHORIZATION_COOKIE_NAME)) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }
            }
            if (StringUtils.hasText(token)) {
                JwtAuthenticationClaims claims = this.tokenProvider.validateToken(token);
                if (claims!=null&&claims.getUserId()!=0) {
                    CurrentUserDetails currentUser  = new CurrentUserDetails(claims);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

//            BodyReaderHttpServletRequestWrapper
            if (httpServletRequest instanceof BodyReaderHttpServletRequestWrapper && ((BodyReaderHttpServletRequestWrapper)httpServletRequest).forward(response)) {
                ///...
            } else {
                filterChain.doFilter(httpServletRequest, response);
            }
        }
        catch (TokenExpiredException e) {
            output((HttpServletResponse) response, buildErrorEntryJsonString(ErrorCode.ERROR_TOKEN_EXPIRED,null));
        }
        catch (Exception e) {
            if (e instanceof NestedServletException && e.getCause()!=null && e.getCause() instanceof AccessDeniedException) {
//                String jsonString = buildErrorEntryJsonString(ErrorCode.ERROR_ACCESS_DENIED);
                output((HttpServletResponse) response, buildErrorEntryJsonString(ErrorCode.ERROR_ACCESS_DENIED,null));
            } else {
                e.printStackTrace();
            }
        }
    }

    static ObjectMapper objectMapper;
    static synchronized ObjectMapper getObjectMapper() {
        if (objectMapper==null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    static String buildErrorEntryJsonString(ErrorCode.ErrorEntry errorEntry, String exceptionString) {

        ResponseData errorResponse = new ErrorResponse(errorEntry);
        if (exceptionString!=null&&exceptionString.length()>0) {
            errorResponse.put("exceptionString",exceptionString!=null?exceptionString:"");
        }
        try {
            return getObjectMapper().writeValueAsString(errorResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void output(HttpServletResponse response, String jsonString) {
        if (jsonString!=null) {
            try {
                response.setCharacterEncoding("utf8");
                response.setContentType("application/json");
                PrintWriter out = null;
                out = response.getWriter();
                out.print(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
