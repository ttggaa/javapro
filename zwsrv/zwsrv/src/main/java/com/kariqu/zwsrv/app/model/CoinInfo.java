package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Coins;

/**
 * Created by simon on 17/12/17.
 */
public class CoinInfo extends BaseModel {

    private int coinsId;
    private int unitAmount;
    private int unitCoins;
    private int isPromotion;

    public CoinInfo() {

    }

    public CoinInfo(Coins coins) {
        this.coinsId=coins.getCoinsId();
        this.unitAmount=coins.getUnitAmount();
        this.unitCoins=coins.getUnitCoins();
        this.isPromotion=coins.getIsPromotion();
    }

    public int getCoinsId() {
        return coinsId;
    }

    public void setCoinsId(int coinsId) {
        this.coinsId = coinsId;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(int unitAmount) {
        this.unitAmount = unitAmount;
    }

    public int getUnitCoins() {
        return unitCoins;
    }

    public void setUnitCoins(int unitCoins) {
        this.unitCoins = unitCoins;
    }

    public int getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
    }
}
