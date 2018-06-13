package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Game;

/**
 * Created by simon on 08/12/17.
 */
public class GameStatusInfo extends BaseModel {
    private int gameId;
    private int isExchange; //0初始状态,1兑换积分
    private int exchangeStatus; //0初始状态,1已换积分
    private int isDelivery; //0初始状态,1需要邮寄(寄存中)
    private int shippingStatus; //0初始状态,1已邮寄配送中
    private int signinStatus;//0初始状态,1已签收


    public GameStatusInfo() {

    }

    public GameStatusInfo(Game game) {
        this.gameId=game.getGameId();
        this.isExchange=game.getIsExchange();
        this.exchangeStatus=game.getExchangeStatus();
        this.isDelivery=game.getIsDelivery();
        this.shippingStatus=game.getShippingStatus();
        this.signinStatus=game.getSigninStatus();
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getIsExchange() {
        return isExchange;
    }

    public void setIsExchange(int isExchange) {
        this.isExchange = isExchange;
    }

    public int getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(int exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    public int getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(int isDelivery) {
        this.isDelivery = isDelivery;
    }

    public int getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(int shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public int getSigninStatus() {
        return signinStatus;
    }

    public void setSigninStatus(int signinStatus) {
        this.signinStatus = signinStatus;
    }
}
