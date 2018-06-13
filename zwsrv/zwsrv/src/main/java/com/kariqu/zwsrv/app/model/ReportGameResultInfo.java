package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;

/**
 * Created by simon on 27/11/17.
 */
public class ReportGameResultInfo extends BaseModel {

//    public static final int GAME_RESULT_INITIAL = 0;//初始状态
//    public static final int GAME_RESULT_SUCCESS = 1;//成功
//    public static final int GAME_RESULT_FAILURE = 2;//失败

    private int gameId; //
    private int roomId; //
    private int playerId; //当前玩家ID
    private String nickName;
    private String avatar;
    private int isInUnlimit; //是否进入无线模式
    private int result;//结果


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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIsInUnlimit() {
        return isInUnlimit;
    }

    public void setIsInUnlimit(int isInUnlimit) {
        this.isInUnlimit = isInUnlimit;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
