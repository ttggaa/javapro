package com.cgy.security.utility;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {
    public static final ResponseData Success = new ResponseData();

    private int code;
    private String msg;
    private Map<String, Object> data;

    public ResponseData() {
        this.code = 0;
        this.msg = "success";
        this.data = new HashMap<>();
    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = new HashMap<>();
    }

    public ResponseData put(String key, Object obj) {
        data.put(key, obj);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
