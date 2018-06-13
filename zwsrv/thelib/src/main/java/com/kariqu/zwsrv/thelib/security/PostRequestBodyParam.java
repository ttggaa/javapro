package com.kariqu.zwsrv.thelib.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 13/12/17.
 */
public class PostRequestBodyParam {

    private String module = "";
    private String path = "";
    private Map<String,Object> params = new HashMap<>();

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
