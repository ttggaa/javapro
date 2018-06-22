package com.kariqu.tyt.http.model;

import com.kariqu.tyt.common.persistence.entity.SignInEntity;

public class SigninConfigModel {
    private int index;
    private int coins;

    public SigninConfigModel() {
        this.index = 0;
        this.coins = 0;
    }

    public SigninConfigModel(SignInEntity entity) {
        this.index = entity.getId();
        this.coins = entity.getCoins();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
