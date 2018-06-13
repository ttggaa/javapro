package com.kariqu.wssrv.app.room;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kariqu.wssrv.app.room.pkg.RoomCmd;
import com.kariqu.wssrv.app.util.JsonUtil;

/**
 * Created by simon on 10/05/2018.
 */
public class RoomEventCmd {
    private final String connId;
    RoomCmd.CommandBase cmd;

    public RoomEventCmd(String connId, String jsonString) throws Exception {
        this.connId = connId;
        this.cmd = new RoomCmd.CommandBase();
        if (connId == null)
            throw new RuntimeException("RoomEventCmd connid is null");
        if (jsonString == null || jsonString.length() == 0)
            throw new RuntimeException("RoomEventCmd jsonString is null");

        ObjectMapper objectMapper = JsonUtil.objectMapper();
        JsonNode node = objectMapper.readTree(jsonString);
        if (node == null)
            throw new RuntimeException("RoomEventCmd jsonString parse error");

        JsonNode cmdNode = node.get("cmd");
        if (cmdNode == null || !cmdNode.isInt())
            throw new RuntimeException("RoomEventCmd cmd error");

        int cmdInt = cmdNode.asInt();
        this.cmd.setCmd(cmdInt);

        JsonNode dataNode = node.get("data");
        if (dataNode == null)
            throw new RuntimeException("RoomEventCmd dataNode error");

        try {
            Gson gson = new Gson();
            switch (cmdInt) {
                case RoomCmd.CMD_ROOM_CHAT_MESSAGE: {
                    this.cmd = gson.fromJson(jsonString, RoomCmd.CommandChatMessage.class);
                }
                break;
                case RoomCmd.CMD_GET_GAME_ROOM_INFO: {
                    this.cmd = gson.fromJson(jsonString, RoomCmd.CommandGetGameRoomInfoRequest.class);
                }
                break;
                case RoomCmd.CMD_GAME_CONFIRM: {
                    this.cmd = gson.fromJson(jsonString, RoomCmd.CommandGameConfirmRequest.class);
                }
                break;
                case RoomCmd.CMD_GAME_DROP_COINS: {
                    this.cmd = gson.fromJson(jsonString, RoomCmd.CommandDropCoinsRequest.class);
                }
                break;
                case RoomCmd.CMD_GAME_DROP_TIMEOUT_NOTIFICATION: {
                    this.cmd = gson.fromJson(jsonString, RoomCmd.CommandDropTimeoutNotification.class);
                }
                break;
                case RoomCmd.CMD_GAME_WIPER_STOP: {
                    this.cmd = gson.fromJson(jsonString, RoomCmd.CommandGameWiperStop.class);
                }
                break;
                default:
                    throw new RuntimeException(String.format("RoomEventCmd unknown cmd.", cmdInt));
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("json: %s cmd:0x%x %s", jsonString, cmdInt, e.toString()));
        }

        if (cmd == null || !cmd.isValid())
            throw new RuntimeException(String.format("RoomEventCmd cmd json error %s cmd:0x%x", jsonString, cmdInt));
    }

    public String getConnId() {
        return connId;
    }

    public RoomCmd.CommandBase getCmd() {
        return cmd;
    }

}