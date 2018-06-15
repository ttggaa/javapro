package com.kariqu.zwsrv.web.persistance.entityex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class GameUser {

    public static GameUser parseOneRow(Object row) {
        if (row == null)
            return null;
        GameUser record = new GameUser();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setUserId((int) cells[index++]);
        record.setCoins((int) cells[index++]);
        record.setAvailableCoins((int) cells[index++]);
        record.setMoney((int) cells[index++]);
        record.setAvailableMoney((int) cells[index++]);
        record.setPoints((int) cells[index++]);
        record.setAvailablePoints((int) cells[index++]);
        record.setNewUserGift(((Boolean) cells[index++]) ? 1 : 0);
        record.setGiftDatetime((Date) cells[index++]);
        record.setChargeFirstTime(((Boolean) cells[index++]) ? 1 : 0);
        record.setChargeFirstTimeDatetime((Date) cells[index++]);

        return record;
    }

    public static List<GameUser> parseRowList(List rows) {
        List<GameUser> ret = new ArrayList<GameUser>();
        for (Object row : rows) {
            GameUser record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    private int userId;

    private int coins;

    private int availableCoins;

    private int money;

    private int availableMoney;

    private int points;

    private int availablePoints;

    private int newUserGift;

    private Date giftDatetime;

    private int chargeFirstTime;

    private Date chargeFirstTimeDatetime;

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

    public int getNewUserGift() {
        return newUserGift;
    }

    public void setNewUserGift(int newUserGift) {
        this.newUserGift = newUserGift;
    }

    public Date getGiftDatetime() {
        return giftDatetime;
    }

    public void setGiftDatetime(Date giftDatetime) {
        this.giftDatetime = giftDatetime;
    }

    public int getChargeFirstTime() {
        return chargeFirstTime;
    }

    public void setChargeFirstTime(int chargeFirstTime) {
        this.chargeFirstTime = chargeFirstTime;
    }

    public Date getChargeFirstTimeDatetime() {
        return chargeFirstTimeDatetime;
    }

    public void setChargeFirstTimeDatetime(Date chargeFirstTimeDatetime) {
        this.chargeFirstTimeDatetime = chargeFirstTimeDatetime;
    }
}
