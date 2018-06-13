package com.kariqu.zwsrv.thelib.persistance.entity;


import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="tbj_game")
public class TbjGame extends VersionedTimedEntity {
    public TbjGame() {
        this.gameId = 0;
        this.roomId = 0;
        this.roomName = "";
        this.deviceId = "";
        this.roomCost = 0;
        this.userId = 0;
        this.dropCount = 0;
        this.dropCoins = 0;
        this.rewardCount = 0;
        this.rewardCoins = 0;
        this.coinsDelta = 0;
        this.startTime = new Date(0);
        this.endTime = new Date(0);
    }

    @Id
    @Column(name="game_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int gameId;

    @Column(name="room_id", nullable = false)
    private int roomId;

    @Column(name="room_name", nullable = false)
    private String roomName;

    @Column(name="device_id", nullable = false)
    private String deviceId;

    @Column(name="room_cost", nullable = false)
    private int roomCost;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="drop_count", nullable = false)
    private int dropCount;

    @Column(name="drop_coins", nullable = false)
    private int dropCoins;

    @Column(name="reward_count", nullable = false)
    private int rewardCount;

    @Column(name="reward_coins", nullable = false)
    private int rewardCoins;

    @Column(name="coins_delta", nullable = false)
    private int coinsDelta;

    @Column(name="start_time", nullable = false)
    private Date startTime;

    @Column(name="end_time", nullable = false)
    private Date endTime;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getRoomCost() {
        return roomCost;
    }

    public void setRoomCost(int roomCost) {
        this.roomCost = roomCost;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDropCount() {
        return dropCount;
    }

    public void setDropCount(int dropCount) {
        this.dropCount = dropCount;
    }

    public int getDropCoins() {
        return dropCoins;
    }

    public void setDropCoins(int dropCoins) {
        this.dropCoins = dropCoins;
    }

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(int rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public int getCoinsDelta() {
        return coinsDelta;
    }

    public void setCoinsDelta(int coinsDelta) {
        this.coinsDelta = coinsDelta;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
