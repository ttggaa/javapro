package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;

/**
 * Created by simon on 26/11/17.
 */
public class ReportRoomStatusInfo extends BaseModel {

    private int roomId; //
    private int userNum;//用户数
    private int goodsNum; //还有多少礼物
    private int isInUnlimit; //是否进入无线模式
    private int status; //机器状态: '0空闲,1游戏中
    private int maintStatus; //管理状态:0正常 1测试中, 2补货中, 3维护中
    private int isOnline; //0未上线 1上线

    public ReportRoomStatusInfo() {

    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getIsInUnlimit() {
        return isInUnlimit;
    }

    public void setIsInUnlimit(int isInUnlimit) {
        this.isInUnlimit = isInUnlimit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMaintStatus() {
        return maintStatus;
    }

    public void setMaintStatus(int maintStatus) {
        this.maintStatus = maintStatus;
    }
}
