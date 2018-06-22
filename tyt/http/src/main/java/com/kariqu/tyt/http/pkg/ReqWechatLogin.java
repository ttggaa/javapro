package com.kariqu.tyt.http.pkg;

public class ReqWechatLogin {
    private String openId;
    private String avatarUrl;
    private String nickName;

    public ReqWechatLogin() {
        this.openId = "";
        this.avatarUrl = "";
        this.nickName = "";
    }

    public boolean isValid() {
        if (openId == null || openId.isEmpty())
            return false;
        if (avatarUrl == null)
            return false;
        if (nickName == null)
            return false;
        return true;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
