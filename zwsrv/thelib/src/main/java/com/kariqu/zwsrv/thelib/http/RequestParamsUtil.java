package com.kariqu.zwsrv.thelib.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 5/4/16.
 */
public class RequestParamsUtil {

    Map<String,Object> allRequestParams;

    public RequestParamsUtil(Map<String, Object> allRequestParams) {
        Map<String,Object> allRequestParamsNew = new HashMap<String,Object>();
        for (Map.Entry<String, Object> entry : allRequestParams.entrySet()) {
            if (entry.getValue() instanceof String) {
                String str = (String)entry.getValue();
                str = urlDecode(str);
                allRequestParamsNew.put(entry.getKey(),str);
            }
        }
        this.allRequestParams = allRequestParamsNew;
    }

    protected String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean validateInputParams(String ...fields){
        for(String key: fields){
            if(this.allRequestParams.get(key) == null) {
                return false;
            }
        }
        return true;
    }

    public String getStringValue(String key) {
        return getStringValue(key,"");
    }
    public String getStringValue(String key, String defaultValue) {
        if (allRequestParams.get(key)!=null) {
            return (String)allRequestParams.get(key);
        }
        return defaultValue;
    }

    public int getInegerValue(String key) {
        return getInegerValue(key, 0);
    }
    public int getInegerValue(String key, int defaultValue) {
        if (allRequestParams.get(key)!=null) {
            return Integer.valueOf((String)allRequestParams.get(key));
        }
        return defaultValue;
    }

    public long getLongValue(String key) {
        return getLongValue(key,0);
    }
    public long getLongValue(String key, long defaultValue) {
        if (allRequestParams.get(key)!=null) {
            return Long.valueOf((String)allRequestParams.get(key));
        }
        return defaultValue;
    }
}
