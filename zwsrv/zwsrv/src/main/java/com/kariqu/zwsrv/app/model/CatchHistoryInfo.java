package com.kariqu.zwsrv.app.model;

public class CatchHistoryInfo {

    private int userId;
    private String nickName;
    private String userIcon;
    private int roomId;         //房间ID
    private String roomName;    //房间名
    private String roomIcon;
    private long timestamp;     //中奖时间

    public CatchHistoryInfo()
    {
        this.userId = 0;
        this.nickName = "";
        this.userIcon = "";
        this.roomId = 0;
        this.roomName = "";
        this.roomIcon = "";
        this.timestamp = 0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
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

    public String getRoomIcon() {
        return roomIcon;
    }

    public void setRoomIcon(String roomIcon) {
        this.roomIcon = roomIcon;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
