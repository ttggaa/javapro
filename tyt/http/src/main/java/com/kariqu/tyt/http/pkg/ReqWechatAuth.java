package com.kariqu.tyt.http.pkg;

public class ReqWechatAuth {
    private String code;

    public ReqWechatAuth() {
        this.code = "";
    }

    public boolean isValid() {
        if (code == null || code.isEmpty())
            return false;
        return true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
