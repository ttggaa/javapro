package com.kariqu.tyt.http.pkg;

public class ReqTouristLogin {
    private String openid;

    public ReqTouristLogin() {
        this.openid = "";
    }

    public boolean isValid() {
        if (openid == null || openid.isEmpty())
            return false;
        return true;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
