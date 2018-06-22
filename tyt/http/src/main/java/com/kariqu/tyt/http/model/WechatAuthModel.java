package com.kariqu.tyt.http.model;

public class WechatAuthModel {
    private String session_key;
    private String openid;

    public WechatAuthModel() {
        this.session_key = "";
        this.openid = "";
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
