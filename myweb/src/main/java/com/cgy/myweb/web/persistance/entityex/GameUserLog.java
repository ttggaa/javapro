package com.kariqu.zwsrv.web.persistance.entityex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class GameUserLog {

    public static GameUserLog parseOneRow(Object row) {
        if (row == null)
            return null;
        GameUserLog record = new GameUserLog();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setLogId((int) cells[index++]);
        record.setUserId((int) cells[index++]);
        record.setCoins((int) cells[index++]);
        record.setAvailableCoins((int) cells[index++]);
        record.setCoinsChangedType((int) cells[index++]);
        record.setMoney((int) cells[index++]);
        record.setAvailableMoney((int) cells[index++]);
        record.setMoneyChangedType((int) cells[index++]);
        record.setPoints((int) cells[index++]);
        record.setAvailablePoints((int) cells[index++]);
        record.setPointsChangedType((int) cells[index++]);
        record.setFee((int) cells[index++]);
        record.setData((String) cells[index++]);
        record.setTimestamp(((BigInteger) cells[index++]).longValue());
        record.setExtraData((String) cells[index++]);

        return record;
    }

    public static List<GameUserLog> parseRowList(List rows) {
        List<GameUserLog> ret = new ArrayList<GameUserLog>();
        for (Object row : rows) {
            GameUserLog record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
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

    public int getCoinsChangedType() {
        return coinsChangedType;
    }

    public void setCoinsChangedType(int coinsChangedType) {
        this.coinsChangedType = coinsChangedType;
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

    public int getMoneyChangedType() {
        return moneyChangedType;
    }

    public void setMoneyChangedType(int moneyChangedType) {
        this.moneyChangedType = moneyChangedType;
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

    public int getPointsChangedType() {
        return pointsChangedType;
    }

    public void setPointsChangedType(int pointsChangedType) {
        this.pointsChangedType = pointsChangedType;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private int logId;

    private int userId;

    private int coins;

    private int availableCoins;

    private int coinsChangedType;

    private int money;

    private int availableMoney;

    private int moneyChangedType;

    private int points;

    private int availablePoints;

    private int pointsChangedType;

    private int fee;

    private String data;

    private long timestamp;

    private String extraData;
}
