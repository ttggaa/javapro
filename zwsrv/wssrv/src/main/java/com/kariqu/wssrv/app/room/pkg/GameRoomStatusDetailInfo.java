package com.kariqu.wssrv.app.room.pkg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 11/05/2018.
 */
public class GameRoomStatusDetailInfo implements PackageChecker {
    private int totalCount; //房间总人数
    private List<GamePlayerInfo> users = new ArrayList<>(); // 房间用户，最多给5条
    //private int queueCount; //当前游戏id
    //private int queueIndex;
    private GamePlayerInfo player = new GamePlayerInfo(); //当前玩家
    private int gameId; //当前游戏id

    @Override
    public boolean isValid() {
        if (users == null)
            return false;
        if (!player.isValid())
            return false;
        return true;
    }

    public GameRoomStatusDetailInfo(List<GamePlayerInfo> users,
                                    GamePlayerInfo player,
                                    int gameId)
    {
        if (users == null) {
            this.totalCount = 0;
        } else {
            this.totalCount = users.size();
            for (int i = users.size() - 1; i >= 0; i--) {
                this.users.add(0, users.get(i));
            }
        }
        this.player.setUserID(player != null ? player.getUserID() : "");
        this.player.setUserName(player != null ? player.getUserName() : "");
        this.gameId=gameId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GamePlayerInfo> getUsers() {
        return users;
    }

    public void setUsers(List<GamePlayerInfo> users) {
        this.users = users;
    }

    public GamePlayerInfo getPlayer() {
        return player;
    }

    public void setPlayer(GamePlayerInfo player) {
        this.player = player;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /*
    public String toJsonString() {
        String jsonString = JsonUtil.convertObject2Json(this);
        if (jsonString==null) {
            jsonString="";
        }
        return jsonString;
    }
    */
}
