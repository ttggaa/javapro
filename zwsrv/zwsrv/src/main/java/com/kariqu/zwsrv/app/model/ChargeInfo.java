package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.persistance.entity.Charge;

public class ChargeInfo {

    private int chargeId;
    private int basicCoins;
    private int extraCoins;
    private int rmb;
    private int leftCornerMark;
    private int rightCornerMark;
    private int chargeFristTime;

    public ChargeInfo(Charge c, int chargeFirstTime)
    {
        this.chargeId = c.getChargeId();
        this.basicCoins = c.getBasicCoins();
        this.extraCoins = c.getExtraCoins();
        this.rmb = c.getRmb();
        this.leftCornerMark = c.getLeftCornerMark();
        this.rightCornerMark = c.getRightCornerMark();
        this.chargeFristTime = chargeFirstTime;
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public int getBasicCoins() {
        return basicCoins;
    }

    public void setBasicCoins(int basicCoins) {
        this.basicCoins = basicCoins;
    }

    public int getExtraCoins() {
        return extraCoins;
    }

    public void setExtraCoins(int extraCoins) {
        this.extraCoins = extraCoins;
    }

    public int getRmb() {
        return rmb;
    }

    public void setRmb(int rmb) {
        this.rmb = rmb;
    }

    public int getLeftCornerMark() {
        return leftCornerMark;
    }

    public void setLeftCornerMark(int leftCornerMark) {
        this.leftCornerMark = leftCornerMark;
    }

    public int getRightCornerMark() {
        return rightCornerMark;
    }

    public void setRightCornerMark(int rightCornerMark) {
        this.rightCornerMark = rightCornerMark;
    }

    public int getChargeFristTime() {
        return chargeFristTime;
    }

    public void setChargeFristTime(int chargeFristTime) {
        this.chargeFristTime = chargeFristTime;
    }
}
