package com.kariqu.wssrv.app.room;

import com.kariqu.wssrv.app.room.pkg.RoomCmd;
import com.kariqu.wssrv.app.util.JsonUtil;

/**
 * Created by simon on 10/05/2018.
 */
public class RoomEventLeaveRoom {
    private final String connId;
    //private final RoomCmd.CommandLeaveGameRoomRequest cmd;

    public RoomEventLeaveRoom(String connId, String jsonString) {
        this.connId = connId;
        if (connId == null)
            throw new RuntimeException("RoomEventLeaveRoom connId is null");
        /*
        if (jsonString == null || jsonString.isEmpty())
            throw new RuntimeException("RoomEventLeaveRoom jsonString is null");
        this.cmd=JsonUtil.convertJson2Model(jsonString,RoomCmd.CommandLeaveGameRoomRequest.class);
        if (this.cmd == null)
            throw new RuntimeException("RoomEventLeaveRoom parse json error");
        if (!this.cmd.isValid())
            throw new RuntimeException("RoomEventLeaveRoom CommandLeaveGameRoomRequest parse error");
            */
    }

    public String getConnId() {
        return connId;
    }

    /*
    public RoomCmd.CommandLeaveGameRoomRequest getCmd() {
        return cmd;
    }
    */
}
