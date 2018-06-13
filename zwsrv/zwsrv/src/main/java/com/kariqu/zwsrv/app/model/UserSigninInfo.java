package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.persistance.entity.UserSignin;

public class UserSigninInfo {
    public UserSigninInfo(UserSignin signin)
    {
        this.alreadSigninId = signin.getSigninId();
    }

    private int canSignin;      // 能否签到
    private int alreadSigninId;     // 已经连续签到天数

    public int getCanSignin() {
        return canSignin;
    }

    public void setCanSignin(int canSignin) {
        this.canSignin = canSignin;
    }

    public int getAlreadSigninId() {
        return alreadSigninId;
    }

    public void setAlreadSigninId(int alreadSigninId) {
        this.alreadSigninId = alreadSigninId;
    }
}
