package com.kariqu.wssrv.app.room;

import com.kariqu.wssrv.app.room.pkg.RoomCmd;
import com.kariqu.wssrv.app.util.JsonUtil;

/**
 * Created by simon on 11/05/2018.
 */
public class RoomEventJoinRoom {
    private final String connId;
    private final RoomCmd.CommandJoinGameRoomRequest cmd;

    public RoomEventJoinRoom(String connId, String jsonString) {
        this.connId = connId;
        if (connId == null)
            throw new RuntimeException("RoomEventJoinRoom connId is null");
        if (jsonString == null || jsonString.length() == 0)
            throw new RuntimeException("RoomEventJoinRoom jsonString is null");
        this.cmd=JsonUtil.convertJson2Model(jsonString,RoomCmd.CommandJoinGameRoomRequest.class);
        if (this.cmd == null)
            throw new RuntimeException("RoomEventJoinRoom parse json error");
        if (!this.cmd.isValid())
            throw new RuntimeException("RoomEventJoinRoom CommandJoinGameRoomRequest parse error");
    }

    public String getConnId() {
        return connId;
    }

    public RoomCmd.CommandJoinGameRoomRequest getCmd() {
        return cmd;
    }
}
