package com.kariqu.zwsrv.app.model;

public class UserSigninConfirmInfo {
    private int rewardCoins;    // 获得娃娃币
    private int signinId;    // 当前签到第几天

    public UserSigninConfirmInfo()
    {
        rewardCoins = 0;
        signinId = 0;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(int rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public int getSigninId() {
        return signinId;
    }

    public void setSigninId(int signinId) {
        this.signinId = signinId;
    }
}
