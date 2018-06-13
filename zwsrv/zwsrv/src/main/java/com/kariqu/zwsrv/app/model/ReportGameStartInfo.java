package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;

/**
 * Created by simon on 07/12/17.
 */
public class ReportGameStartInfo extends BaseModel {

//    public static final int GAME_STATUS_INITIAL = 0;//初始状态
//    public static final int GAME_STATUS_IN_GAME = 1;//游戏中
//    public static final int GAME_STATUS_GAME_OVER = 2;//游戏结束

    private int gameId; //
    private int roomId; //
    private int playerId; //当前玩家ID
    private int isInUnlimit; //是否进入无线模式

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

    public int getIsInUnlimit() {
        return isInUnlimit;
    }

    public void setIsInUnlimit(int isInUnlimit) {
        this.isInUnlimit = isInUnlimit;
    }
}
