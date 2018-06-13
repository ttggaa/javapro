package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;

/**
 * Created by simon on 08/12/17.
 */
public class AccountInfo extends BaseModel {

    private int userId;
    private int coins;
    private int availableCoins;
    private int money;
    private int availableMoney;
    private int points;
    private int availablePoints;
    private int chargeFirstTime;
    private int newUserGiftSeconds;

    public AccountInfo(Account account, int newUserGiftSeconds) {
        this.userId=account.getUserId();
        this.coins=account.getCoins();
        this.availableCoins=account.getAvailableCoins();
        this.money=account.getMoney();
        this.availableMoney=account.getAvailableMoney();
        this.points=account.getPoints();
        this.availablePoints=account.getAvailablePoints();
        this.chargeFirstTime = account.getChargeFirstTime();
        this.newUserGiftSeconds = newUserGiftSeconds;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getAvailableCoins() {
        return availableCoins;
    }

    public void setAvailableCoins(int availableCoins) {
        this.availableCoins = availableCoins;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(int availableMoney) {
        this.availableMoney = availableMoney;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public int getChargeFirstTime() {
        return chargeFirstTime;
    }

    public void setChargeFirstTime(int chargeFirstTime) {
        this.chargeFirstTime = chargeFirstTime;
    }

    public int getNewUserGiftSeconds() {
        return newUserGiftSeconds;
    }

    public void setNewUserGiftSeconds(int newUserGiftSeconds) {
        this.newUserGiftSeconds = newUserGiftSeconds;
    }
}
