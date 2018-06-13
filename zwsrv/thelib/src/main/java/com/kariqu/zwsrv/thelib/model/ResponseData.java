package com.kariqu.zwsrv.thelib.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 5/11/15.
 */
public class ResponseData extends BaseResponseData {

    private Map<String, Object> data;

    public ResponseData(){
        this(0,"success",new HashMap<String, Object>());
    }

    public ResponseData(Map<String, Object> data){
        this(0,"success",data);
    }

    public ResponseData(int code, String msg) {
        this(code,msg,new HashMap<String, Object>());
    }

    public ResponseData(int code, String msg, Map<String, Object> data){
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public ResponseData put(String key, Object value) {
        data.put(key,value);
        return this;
    }

    public ResponseData remove(String key) {
        data.remove(key);
        return this;
    }
}
