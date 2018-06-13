package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.RongToken;

/**
 * Created by simon on 08/12/17.
 */
public class RongTokenInfo extends BaseModel {

    private int userId;
    private String userSN;
    private String nickName;
    private String avatarUrl;
    private String token;

    public RongTokenInfo() {

    }

    public RongTokenInfo(RongToken rongToken) {
        this.userId=rongToken.getUserId();
        this.userSN=rongToken.getUserSN();
        this.nickName=rongToken.getNickName();
        this.avatarUrl=rongToken.getAvatarUrl();
        this.token=rongToken.getToken();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserSN() {
        return userSN;
    }

    public void setUserSN(String userSN) {
        this.userSN = userSN;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
