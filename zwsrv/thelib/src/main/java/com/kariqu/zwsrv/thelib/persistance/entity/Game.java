package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by simon on 06/12/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_game")
public class Game extends VersionedTimedEntity {

    public static final int GAME_PAY_STATUS_INITIAL = 0;//
    public static final int GAME_PAY_STATUS_PAID = 1;//已付款

    public static final int GAME_STATUS_INITIAL = 0;//初始状态
    public static final int GAME_STATUS_IN_GAME = 1;//游戏中
    public static final int GAME_STATUS_GAME_OVER = 2;//游戏结束

    public static final int GAME_RESULT_INITIAL = 0;//初始状态
    public static final int GAME_RESULT_SUCCESS = 1;//成功
    public static final int GAME_RESULT_FAILURE = 2;//失败

    public static final int GAME_GOODS_EXCHANGE_STATUS_INITIAL = 0;//
    public static final int GAME_GOODS_EXCHANGE_STATUS_DONE = 1;//

    public static final int GAME_GOODS_SHIPPING_STATUS_INITIAL = 0;//
    public static final int GAME_GOODS_SHIPPING_STATUS_DELIVERING = 1;//邮寄中

    public Game() {
        gameSN="";
        playerName="";
        name="";
        imageUrl="";
        this.itemId = 0;
    }


    public Game(Room room) {
        this();
        this.gameSN=generateSN();
        this.roomId=room.getRoomId();
        this.roomType=room.getRoomType();
        this.name=room.getName()!=null?room.getName():"";
        this.imageUrl=room.getImageUrl()!=null?room.getImageUrl():"";
        this.isSpecial=room.getIsSpecial();
        this.unlimitTimes=room.getUnlimitTimes();
        this.roomCost=room.getRoomCost();
        this.canDelivery=room.getCanDelivery();
        this.exchangeCoins=room.getExchangeCoins();
        this.rewardCoins=room.getRewardCoins();
        this.points=room.getPoints();
        this.payStatus=GAME_PAY_STATUS_INITIAL;
        this.coinsPaid=0;
        this.roomStatus=room.getStatus();
        this.roomMaintStatus=room.getMaintStatus();
        this.roomIsOnline=room.getIsOnline();
        this.startTime=0;
        this.endTime=0;
        this.status=GAME_STATUS_INITIAL;
        this.result=GAME_RESULT_INITIAL;
        this.isExchange=0;
        this.exchangeStatus=GAME_GOODS_EXCHANGE_STATUS_INITIAL;
        this.isDelivery=0;
        this.shippingStatus=0;
        this.itemId = 0;
    }

    public Game(Room room, int playerId, String playerName, int playerRole, int isInUnlimit) {
        this();
        this.gameSN=generateSN();
        this.roomId=room.getRoomId();
        this.roomType=room.getRoomType();
        this.playerId=playerId;
        this.playerName=playerName;
        this.playerRole=playerRole;
        this.name=room.getName()!=null?room.getName():"";
        this.imageUrl=room.getImageUrl()!=null?room.getImageUrl():"";
        this.isSpecial=room.getIsSpecial();
        this.unlimitTimes=room.getUnlimitTimes();
        this.roomCost=room.getRoomCost();
        this.canDelivery=room.getCanDelivery();
        this.exchangeCoins=room.getExchangeCoins();
        this.rewardCoins=room.getRewardCoins();
        this.points=room.getPoints();
        this.payStatus=GAME_PAY_STATUS_INITIAL;
        this.coinsPaid=0;
        this.roomStatus=room.getStatus();
        this.roomMaintStatus=room.getMaintStatus();
        this.roomIsOnline=room.getIsOnline();
        this.isInUnlimit=isInUnlimit;
        this.startTime=0;
        this.endTime=0;
        this.status=GAME_STATUS_INITIAL;
        this.result=GAME_RESULT_INITIAL;
        this.isExchange=0;
        this.exchangeStatus=0;
        this.isDelivery=0;
        this.shippingStatus=GAME_GOODS_SHIPPING_STATUS_INITIAL;
        this.itemId = 0;
    }

    @Id
    @Column(name="game_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int gameId;

    @Column(name="game_sn", nullable = false)
    private String gameSN;

    @Column(name="room_id", nullable = false)
    private int roomId;

    @Column(name="room_type", nullable = false)
    private int roomType;

    @Column(name="player_id", nullable = false)
    private int playerId;

    @Column(name="player_name", nullable = false)
    private String playerName;

    @Column(name="player_role", nullable = false)
    private int playerRole;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="is_special", nullable = false)
    private int isSpecial;

    @Column(name="unlimit_times", nullable = false)
    private int unlimitTimes;

    @Column(name="room_cost", nullable = false)
    private int roomCost;

    @Column(name="can_delivery", nullable = false)
    private int canDelivery;

    @Column(name="exchange_coins", nullable = false)
    private int exchangeCoins;

    @Column(name="reward_coins", nullable = false)
    private int rewardCoins;

    @Column(name="points", nullable = false)
    private int points;

    @Column(name="pay_way", nullable = false)
    private int payWay;

    @Column(name="pay_status", nullable = false)
    private int payStatus;

    @Column(name="coins_paid", nullable = false)
    private int coinsPaid;

    @Column(name="points_paid", nullable = false)
    private int pointsPaid;

    @Column(name="room_status", nullable = false)
    private int roomStatus;

    @Column(name="room_maint_status", nullable = false)
    private int roomMaintStatus;

    @Column(name="room_is_online", nullable = false)
    private int roomIsOnline;

    @Column(name="is_in_unlimit", nullable = false)
    private int isInUnlimit;

    @Column(name="start_time", nullable = false)
    private long startTime;

    @Column(name="end_time", nullable = false)
    private long endTime;

    @Column(name="status", nullable = false)
    private int status;

    @Column(name="result", nullable = false)
    private int result;

    @Column(name="is_exchange", nullable = false)
    private int isExchange;

    @Column(name="exchange_status", nullable = false)
    private int exchangeStatus;

    @Column(name="is_delivery", nullable = false)
    private int isDelivery;

    @Column(name="shipping_status", nullable = false)
    private int shippingStatus;

    @Column(name="signin_status", nullable = false)
    private int signinStatus;

    @Column(name="item_id", nullable = false)
    private int itemId;

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

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
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

    public int getPointsPaid() {
        return pointsPaid;
    }

    public void setPointsPaid(int pointsPaid) {
        this.pointsPaid = pointsPaid;
    }

    public int getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }

    public int getRoomMaintStatus() {
        return roomMaintStatus;
    }

    public void setRoomMaintStatus(int roomMaintStatus) {
        this.roomMaintStatus = roomMaintStatus;
    }

    public int getRoomIsOnline() {
        return roomIsOnline;
    }

    public void setRoomIsOnline(int roomIsOnline) {
        this.roomIsOnline = roomIsOnline;
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public static String generateSN() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}


//CREATE TABLE `zww_game` (
//        `game_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `game_sn` varchar(32) NOT NULL DEFAULT '',
//        `room_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `room_type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `player_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `player_name` varchar(80) NOT NULL DEFAULT '',
//        `player_role` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `name` varchar(80) NOT NULL DEFAULT '',
//        `image_url` varchar(255) NOT NULL DEFAULT '',
//        `is_special` tinyint(1) NOT NULL DEFAULT '0',
//        `unlimit_times` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `room_cost` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '单次多少金币',
//        `can_delivery` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '可否邮寄',
//        `exchange_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '兑换金币数,如果为0则不能兑换金币',
//        `reward_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '奖励金币数',
//        `points` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '积分',
//        `pay_way` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1.积分支付 2.金币支付',
//        `pay_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始状态,1已支付',
//        `coins_paid` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `points_paid` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `room_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0空闲,1游戏中,2游戏无限模式中',
//        `room_maint_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1测试中,2补货中,3维护中',
//        `room_is_online` tinyint(1) NOT NULL DEFAULT '0',
//        `is_in_unlimit` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否在无线模式抓取',
//        `start_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `end_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始状态,1游戏中,2游戏结束',
//        `result` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始,1成功,2失败',
//        `is_exchange` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始状态,1兑换积分',
//        `exchange_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始状态,1已换积分',
//        `is_delivery` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始状态,1需要邮寄(寄存中)',
//        `shipping_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始状态,1已邮寄配送中',
//        `signin_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0初始状态,1已签收',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`game_id`),
//        UNIQUE KEY `game_sn` (`game_sn`),
//        KEY `room_id` (`room_id`),
//        KEY `player_id` (`player_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;
