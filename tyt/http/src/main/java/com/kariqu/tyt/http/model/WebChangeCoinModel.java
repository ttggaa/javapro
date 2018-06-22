package com.kariqu.tyt.http.model;

public class WebChangeCoinModel {
    private int userId;
    private int changeCoin;
    private int remainCoin;
    private int previousCoin;

    public WebChangeCoinModel() {
        this.userId = 0;
        this.changeCoin = 0;
        this.remainCoin = 0;
        this.previousCoin = 0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChangeCoin() {
        return changeCoin;
    }

    public void setChangeCoin(int changeCoin) {
        this.changeCoin = changeCoin;
    }

    public int getRemainCoin() {
        return remainCoin;
    }

    public void setRemainCoin(int remainCoin) {
        this.remainCoin = remainCoin;
    }

    public int getPreviousCoin() {
        return previousCoin;
    }

    public void setPreviousCoin(int previousCoin) {
        this.previousCoin = previousCoin;
    }
}
