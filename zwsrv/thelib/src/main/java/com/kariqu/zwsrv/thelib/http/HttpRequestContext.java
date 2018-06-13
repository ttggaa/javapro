package com.kariqu.zwsrv.thelib.http;


import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.util.AESCoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by simon on 5/5/16.
 */
public class HttpRequestContext extends HttpServletRequestWrapper {

    private Map<String,String> allRequestParams;

    public HttpRequestContext(HttpServletRequest request, Map<String, String> allRequestParams) {
        super(request);

        String requestType = request.getMethod().toUpperCase();
        String contextPath = request.getContextPath();
        String requestUri = this.extractRequestUri(request.getRequestURI(), contextPath);
        String encoding = "UTF-8";
        String servletExtension = "";

        boolean isPost = "POST".equals(requestType);
        boolean isGet = !isPost;

        Map<String,String> allRequestParamsNew = new HashMap<String,String>();
        for (Map.Entry<String, String> entry : allRequestParams.entrySet()) {
            if (entry.getValue() instanceof String) {
                String str = (String)entry.getValue();
                str = urlDecode(str);
                allRequestParamsNew.put(entry.getKey(),str);
            }
        }
        this.allRequestParams = allRequestParamsNew;
    }

    private String extractRequestUri(String requestUri, String contextPath) {
        // First, remove the context path from the requestUri,
        // so we can work only with the important stuff
        if (contextPath != null && contextPath.length() > 0) {
            requestUri = requestUri.substring(contextPath.length(), requestUri.length());
        }

        // Remove the "jsessionid" (or similar) from the URI
        // Probably this is not the right way to go, since we're
        // discarding the value...
        int index = requestUri.indexOf(';');

        if (index > -1) {
            int lastIndex = requestUri.indexOf('?', index);

            if (lastIndex == -1) {
                lastIndex = requestUri.indexOf('&', index);
            }

            if (lastIndex == -1) {
                requestUri = requestUri.substring(0, index);
            }
            else {
                String part1 = requestUri.substring(0, index);
                requestUri = part1 + requestUri.substring(lastIndex);
            }
        }

        return requestUri;
    }

    protected String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getContextPath()
    {
        String contextPath = super.getContextPath();
        return contextPath;
    }

    public String getRemoteAddr()
    {
        // We look if the request is forwarded
        // If it is not call the older function.
        String ip = super.getHeader("x-forwarded-for");

        if (ip == null) {
            ip = super.getRemoteAddr();
        }
        else {
            // Process the IP to keep the last IP (real ip of the computer on the net)
            StringTokenizer tokenizer = new StringTokenizer(ip, ",");

            // Ignore all tokens, except the last one
            for (int i = 0; i < tokenizer.countTokens() -1 ; i++) {
                tokenizer.nextElement();
            }

            ip = tokenizer.nextToken().trim();

            if (ip.equals("")) {
                ip = null;
            }
        }

        // If the ip is still null, we put 0.0.0.0 to avoid null values
        if (ip == null) {
            ip = "0.0.0.0";
        }

        return ip;
    }

    public HttpRequestContext validateInputParams(String ...fields) throws ServerException {
        for(String key: fields){
            Object value = this.allRequestParams.get(key);
            if(value == null) {
                throw new ServerException(ErrorCode.ERROR_INVALID_PARAMETERS);
            }
        }
        return this;
    }

    public String decrypt(String str, String decryptKey) throws ServerException {
        if (str!=null) {
            try {
                return AESCoder.decrypt(decryptKey.getBytes(), str);
            } catch (Exception e) {
                throw  new ServerException(ErrorCode.ERROR_INVALID_PARAMETERS);
            }
        }
        return str;
    }

    public boolean requestParamsContains(String key) {
        if (allRequestParams!=null&&allRequestParams.get(key)!=null) {
            return true;
        }
        return false;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value==null) {
            value = "";
        }
        return value;
    }

    public String getStringValue(String key) throws ServerException {
        return getStringValue(key,"");
    }

    public String getStringValue(String key, String decryptKey) throws ServerException {
        return getStringValue(key,"",decryptKey);
    }

    public String getStringValue(String key, String defaultValue, String decryptKey) throws ServerException {
        if (allRequestParams!=null&&allRequestParams.get(key)!=null) {
            String value = (String)allRequestParams.get(key);
            if (decryptKey!=null&&decryptKey.length()>0) {
                value = decrypt(value,decryptKey);
            }
            return value;
        }
        return defaultValue;
    }

    public int getInegerValue(String key) throws ServerException {
        return getInegerValue(key,"");
    }

    public int getInegerValue(String key, String decryptKey) throws ServerException {
        return getInegerValue(key,0,decryptKey);
    }

    public int getInegerValue(String key, int defaultValue, String decryptKey) throws ServerException {
        if (allRequestParams!=null&&allRequestParams.get(key)!=null) {
            String value = (String)allRequestParams.get(key);
            if (decryptKey!=null&&decryptKey.length()>0) {
                value = decrypt(value,decryptKey);
            }
            return Integer.valueOf(value);
        }
        return defaultValue;
    }

    public long getLongValue(String key) throws ServerException {
        return getLongValue(key,"");
    }

    public long getLongValue(String key, String decryptKey) throws ServerException {
        return getLongValue(key,0,decryptKey);
    }

    public long getLongValue(String key, long defaultValue, String decryptKey) throws ServerException {
        if (allRequestParams!=null&&allRequestParams.get(key)!=null) {
            String value = (String)allRequestParams.get(key);
            if (decryptKey!=null&&decryptKey.length()>0) {
                value = decrypt(value,decryptKey);
            }
            return Long.valueOf(value);
        }
        return defaultValue;
    }
}
