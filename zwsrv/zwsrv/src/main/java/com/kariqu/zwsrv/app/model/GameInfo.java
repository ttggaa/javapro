package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Game;

/**
 * Created by simon on 07/12/17.
 */
public class GameInfo extends BaseModel {

    public GameInfo(Game game) {
        this.gameId=game.getGameId();
        this.gameSN=game.getGameSN();
        this.roomId=game.getRoomId();
        this.roomType=game.getRoomType();
        this.playerId=game.getPlayerId();
        this.playerName=game.getPlayerName();
        this.playerRole=game.getPlayerRole();
        this.name=game.getName();

        this.isSpecial=game.getIsSpecial();
        this.unlimitTimes=game.getUnlimitTimes();
        this.roomCost=game.getRoomCost();
        this.canDelivery=game.getCanDelivery();
        this.exchangeCoins=game.getExchangeCoins();
        this.rewardCoins=game.getRewardCoins();
        this.points=game.getPoints();
        this.payWay=game.getPayWay();
        this.payStatus=game.getPayStatus();
        this.coinsPaid=game.getCoinsPaid();
        this.pointsPaid=game.getPointsPaid();
        this.isInUnlimit=game.getIsInUnlimit();
        this.startTime=game.getStartTime();
        this.endTime=game.getEndTime();
        this.status=game.getStatus();
        this.result=game.getResult();
        this.isExchange=game.getIsExchange();
        this.exchangeStatus=game.getExchangeStatus();
        this.isDelivery=game.getIsDelivery();
        this.shippingStatus=game.getShippingStatus();
        this.signinStatus=game.getSigninStatus();

        this.imageUrl=URLHelper.fullUrl(game.getImageUrl());
    }

    private int gameId;
    private String gameSN;
    private int roomId;
    private int roomType;
    private int playerId;
    private String playerName;
    private int playerRole;
    private String name;
    private String imageUrl;
    private int isSpecial;
    private int unlimitTimes;
    private int roomCost;
    private int canDelivery;
    private int exchangeCoins;
    private int rewardCoins;
    private int points;
    private int payWay;
    private int payStatus; //0初始状态,1已支付
    private int coinsPaid;
    private int pointsPaid;
    private int isInUnlimit; //是否在无线模式抓取
    private long startTime;
    private long endTime;
    private int status; //0初始状态,1游戏中,2游戏结束
    private int result; //0初始,1成功,2失败
    private int isExchange; //0初始状态,1兑换积分
    private int exchangeStatus; //0初始状态,1已换积分
    private int isDelivery; //0初始状态,1需要邮寄(寄存中)
    private int shippingStatus; //0初始状态,1已邮寄配送中
    private int signinStatus;//0初始状态,1已签收

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameSN() {
        return gameSN;
    }

    public void setGameSN(String gameSN) {
        this.gameSN = gameSN;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(int playerRole) {
        this.playerRole = playerRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(int isSpecial) {
        this.isSpecial = isSpecial;
    }

    public int getUnlimitTimes() {
        return unlimitTimes;
    }

    public void setUnlimitTimes(int unlimitTimes) {
        this.unlimitTimes = unlimitTimes;
    }

    public int getRoomCost() {
        return roomCost;
    }

    public void setRoomCost(int roomCost) {
        this.roomCost = roomCost;
    }

    public int getCanDelivery() {
        return canDelivery;
    }

    public void setCanDelivery(int canDelivery) {
        this.canDelivery = canDelivery;
    }

    public int getExchangeCoins() {
        return exchangeCoins;
    }

    public void setExchangeCoins(int exchangeCoins) {
        this.exchangeCoins = exchangeCoins;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(int rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getCoinsPaid() {
        return coinsPaid;
    }

    public void setCoinsPaid(int coinsPaid) {
        this.coinsPaid = coinsPaid;
    }

    public int getIsInUnlimit() {
        return isInUnlimit;
    }

    public void setIsInUnlimit(int isInUnlimit) {
        this.isInUnlimit = isInUnlimit;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getPointsPaid() {
        return pointsPaid;
    }

    public void setPointsPaid(int pointsPaid) {
        this.pointsPaid = pointsPaid;
    }
}
