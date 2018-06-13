package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;

public class UserCardRewardInfo extends BaseModel {

    private int coins;
    private int points;

    public UserCardRewardInfo(int coins, int points) {
        this.coins = coins;
        this.points = points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
