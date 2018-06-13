package com.kariqu.wssrv.app.room;

import com.kariqu.wssrv.app.room.pkg.GamePlayerInfo;

public class TbjPlayer {
    private GamePlayerInfo playerInfo;
    private String connId;
    private GameRoom room;

    public TbjPlayer(String connId, GamePlayerInfo playerInfo)
    {
        this.connId = connId;
        this.playerInfo = playerInfo;
        this.room = null;
    }

    public String userId() {
        return playerInfo != null ? playerInfo.getUserID() : "";
    }

    public GamePlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(GamePlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }

    public GameRoom getRoom() {
        return room;
    }

    public void setRoom(GameRoom room) {
        this.room = room;
    }
}
