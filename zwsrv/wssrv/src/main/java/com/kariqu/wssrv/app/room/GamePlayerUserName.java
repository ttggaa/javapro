package com.kariqu.wssrv.app.room;

/**
 * Created by simon on 10/05/2018.
 */
public class GamePlayerUserName {
    private String nickName;
    private String avatar;

    public GamePlayerUserName() {
        nickName="";
        avatar="";
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
