package com.kariqu.wssrv.app.room.pkg;

import com.google.gson.Gson;

/**
 * Created by simon on 09/05/2018.
 */
public class RoomCmd {

    static final public int CMD_ROOM_CHAT_MESSAGE = 0x506;  //聊天消息

    static final public int CMD_GAME_ROOM_UPDATE_NOTIFICATION = 0x101;    // 房间成员更新（Server-->Client）

    static final public int CMD_GET_GAME_ROOM_INFO = 0x201;  // 获取游戏信息，包括总人数，排队列表，当前谁在玩，单局游戏时长（Client-->Server）

    static final public int CMD_GET_GAME_ROOM_INFO_RESPONSE = 0x202;    // 回复获取房间信息指令，包括总人数，排队列表，当前谁在玩（Server-->Client）

    //static final public int CMD_APPLY = 0x301;          // 预约上机申请（Client-->Server）

    //static final public int CMD_APPLY_RESPONSE = 0x302;  // Server回复收到预约申请，并告知预约结果（Server-->Client）

    //static final public int CMD_CANCEL_APPLY = 0x303;   // 取消预约(暂时不用)（Client-->Server）

    //static final public int CMD_CANCEL_APPLY_RESPONSE = 0x304;   // 回复收到取消预约指令（Server-->Client）

    //static final public int CMD_GAME_READY_NOTIFICATION = 0x305;     // 通知某人准备上机, 此时用户可使用 CMD_ABANDON_PLAY 放弃游戏（Server-->Client）

    //static final public int CMD_GAME_READY_REPLY = 0x306; // Client回复收到上机指令（Client-->Server）

    static final public int CMD_GAME_CONFIRM = 0x307;    // 确认上机或者放弃玩游戏，仅在正式开始玩之前发送此指令有效，即在收到服务端的 CMD_GAME_READY 指令时，通过该指令告诉服务端开始玩还是放弃（Client-->Server）

    static final public int CMD_GAME_CONFIRM_RESPONSE = 0x308;    // Server回复收到确认上机或者放弃玩游戏指令（Server-->Client）

    static final public int CMD_GAME_REWARD_COINS_NOTIFICATION = 0x401;  // 广播奖励金币（Server-->Client）

    //static final public int CMD_GAME_REWARD_COINS_REPLY = 0x402;  // 回复收到奖励金币（Client-->Server）

    static final public int CMD_GAME_DROP_COINS = 0x403;  // 推币（Client-->Server）

    static final public int CMD_GAME_DROP_COINS_RESPONSE = 0x404;  // 回复推币结果（Server-->Client）

    static final public int CMD_GAME_WIPER_STOP = 0x405;  // 启动/停止雨刷（Client-->Server）

    static final public int CMD_GAME_DROP_TIMEOUT_NOTIFICATION = 0x501;  // 游戏结束,下机（Client-->Server）

    static final public int CMD_GAME_DROP_TIMEOUT_REPLY = 0x502;

    static final public int CMD_GAME_OVER_NOTIFICATION = 0x503;  // 服务端通知下机（Server-->Client）



    ///////////Error Code
    static final public int EC_SUCCESS = 0;
    static final public int EC_PLAYER_NOT_IN_ROOM = 1;          // 玩家不再房间中
    static final public int EC_OTHER_PLAYER_IN_GAME = 2;        // 其他玩家正在上机
    static final public int EC_IN_GAME_COIN_INSUFFICIENT = 3;   // 上机金币不足
    static final public int EC_PLAYER_NOT_IN_GAME = 4;          // 此玩家没有上机
    static final public int EC_DROP_COIN_INSUFFICIENT = 5;      // 投币金币不足
    static final public int EC_ROOM_OFFLINE = 6;                // 房间已经下限
    static final public int EC_ROOM_IS_NULL = 7;                // 房间不存在


    //推币,中奖,加倒计时 30s(注意:容错断线重连)

    //Client-->Server 进入房间
    public static class CommandJoinGameRoomRequest implements PackageChecker {
        private String roomId;
        private String roomName;
        private String deviceId;
        private GamePlayerInfo playerInfo;

        @Override
        public boolean isValid() {
            if (this.roomId == null || this.roomId.isEmpty())
                return false;
            if (this.roomName == null || this.roomName.isEmpty())
                return false;
            if (this.deviceId == null || this.deviceId.isEmpty())
                return false;
            if (!playerInfo.isValid())
                return false;
            return true;
        }

        public CommandJoinGameRoomRequest() {
            this.roomId = "";
            this.roomName = "";
            this.deviceId = "";
            this.playerInfo = null;
        }
        public CommandJoinGameRoomRequest(String roomId, String roomName, String deviceId, GamePlayerInfo playerInfo) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.deviceId = deviceId;
            this.playerInfo = playerInfo;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
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

        public GamePlayerInfo getPlayerInfo() {
            return playerInfo;
        }

        public void setPlayerInfo(GamePlayerInfo playerInfo) {
            this.playerInfo = playerInfo;
        }

        @Override
        public String toString()
        {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public static class CommandJoinGameRoomResponse implements PackageChecker {
        private String roomId;
        private GameRoomStatusDetailInfo statusDetailInfo;
        private int errorCode;

        @Override
        public boolean isValid() {
            if (roomId == null || roomId.isEmpty())
                return false;
            if (!statusDetailInfo.isValid())
                return false;
            return true;
        }

        public CommandJoinGameRoomResponse(String roomId, GameRoomStatusDetailInfo statusDetailInfo) {
            this.roomId=roomId;
            this.statusDetailInfo=statusDetailInfo;
            this.errorCode = 0;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public GameRoomStatusDetailInfo getStatusDetailInfo() {
            return statusDetailInfo;
        }

        public void setStatusDetailInfo(GameRoomStatusDetailInfo statusDetailInfo) {
            this.statusDetailInfo = statusDetailInfo;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }
    }

    //Client-->Server 离开房间
    public static class CommandLeaveGameRoomRequest implements PackageChecker {
        private String roomId;

        @Override
        public boolean isValid() {
            if (roomId == null || roomId.isEmpty())
                return false;
            return true;
        }

        public CommandLeaveGameRoomRequest(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }
    }

    public static class CommandLeaveGameRoomResponse implements PackageChecker {
        @Override
        public boolean isValid() {
            return true;
        }
    }


    //================================================================
    public static class CommandData implements PackageChecker {

        @Override
        public boolean isValid() {
            if (roomId == null || roomId.isEmpty())
                return false;
            return true;
        }

        public CommandData() {
            this.roomId = "";
        }

        public CommandData(String roomId) {
            this.roomId=roomId;
        }

        private String roomId;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }
    }

    public static class CommandBase implements PackageChecker {
        protected int cmd;

        public CommandBase() {
            this.cmd = 0;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public int getCmd() {
            return cmd;
        }

        public void setCmd(int cmd) {
            this.cmd = cmd;
        }

        @Override
        public String toString()
        {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public static class CommandResponseBase extends CommandBase {
        protected int errorCode;

        public CommandResponseBase() {
            super();
            this.errorCode = EC_SUCCESS;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }
    }

    //================================================================
    //Server-->Client 房间成员更新
    public static class CommandGameRoomUpdateNotificationData extends CommandData {
        private GameRoomStatusDetailInfo statusDetailInfo;

        @Override
        public boolean isValid() {
            if (!super.isValid())
                return false;
            if (statusDetailInfo == null || !statusDetailInfo.isValid())
                return false;
            return true;
        }

        public CommandGameRoomUpdateNotificationData(String roomId, GameRoomStatusDetailInfo statusDetailInfo) {
            super(roomId);
            this.statusDetailInfo=statusDetailInfo;
        }

        public GameRoomStatusDetailInfo getStatusDetailInfo() {
            return statusDetailInfo;
        }

        public void setStatusDetailInfo(GameRoomStatusDetailInfo statusDetailInfo) {
            this.statusDetailInfo = statusDetailInfo;
        }
    }

    public static class CommandGameRoomUpdateNotification extends CommandBase {
        private CommandGameRoomUpdateNotificationData data;
        public CommandGameRoomUpdateNotification(String roomId, GameRoomStatusDetailInfo statusDetailInfo) {
            this.cmd=CMD_GAME_ROOM_UPDATE_NOTIFICATION;
            this.data=new CommandGameRoomUpdateNotificationData(roomId,statusDetailInfo);
        }

        public CommandGameRoomUpdateNotificationData getData() {
            return data;
        }
    }



    //================================================================
    //Client-->Server 获取房间信息指令
    public static class CommandGetGameRoomInfoRequest extends CommandBase {
        private CommandData data;
        public CommandGetGameRoomInfoRequest(String roomId) {
            this.cmd=CMD_GET_GAME_ROOM_INFO;
            this.data=new CommandData(roomId);
        }
    }
    //Server-->Client 回复获取房间信息指令
    public static class CommandGetGameRoomInfoResponseData extends CommandData {
        protected GameRoomStatusDetailInfo statusDetailInfo;

        @Override
        public boolean isValid() {
            if (super.isValid())
                return false;
            if (statusDetailInfo == null || !statusDetailInfo.isValid())
                return false;
            return true;
        }

        public CommandGetGameRoomInfoResponseData(String roomId, GameRoomStatusDetailInfo statusDetailInfo) {
            super(roomId);
            this.statusDetailInfo=statusDetailInfo;
        }

        public GameRoomStatusDetailInfo getStatusDetailInfo() {
            return statusDetailInfo;
        }

        public void setStatusDetailInfo(GameRoomStatusDetailInfo statusDetailInfo) {
            this.statusDetailInfo = statusDetailInfo;
        }
    }
    public static class CommandGetGameRoomInfoResponse extends CommandResponseBase {
        private CommandGetGameRoomInfoResponseData data;
        public CommandGetGameRoomInfoResponse(String roomId, GameRoomStatusDetailInfo statusDetailInfo) {
            this.cmd=CMD_GET_GAME_ROOM_INFO_RESPONSE;
            this.data=new CommandGetGameRoomInfoResponseData(roomId,statusDetailInfo);
        }
        public CommandGetGameRoomInfoResponseData getData() {
            return data;
        }
    }

//    //================================================================
//    //Server-->Client 通知某人准备上机
//    public static class CommandGameReadyNotificationData extends CommandData {
//        protected GamePlayerInfo player;
//
//        @Override
//        public boolean isValid() {
//            if (!super.isValid())
//                return false;
//            if (player == null || !player.isValid())
//                return false;
//            return true;
//        }
//
//        public CommandGameReadyNotificationData(String roomId, GamePlayerInfo player) {
//            super(roomId);
//            this.player=player;
//        }
//
//        public GamePlayerInfo getPlayer() {
//            return player;
//        }
//
//        public void setPlayer(GamePlayerInfo player) {
//            this.player = player;
//        }
//    }
//    public static class CommandGameReadyNotification extends CommandBase {
//        private CommandGameReadyNotificationData data;
//        public CommandGameReadyNotification(String roomId, GamePlayerInfo player) {
//            this.cmd=CMD_GAME_READY_NOTIFICATION;
//            this.data = new CommandGameReadyNotificationData(roomId,player);
//        }
//
//        public CommandGameReadyNotificationData getData() {
//            return data;
//        }
//    }
//    public static class CommandGameReadyReply extends CommandBase {
//        private CommandData data;
//        public CommandGameReadyReply(String roomId) {
//            this.cmd=CMD_GAME_READY_REPLY;
//            this.data=new CommandData(roomId);
//        }
//
//        public CommandData getData() {
//            return data;
//        }
//    }
//
//
//
//    //================================================================
//    //Client-->Server 预约上机
//    public static class CommandGameApplyRequest extends CommandBase {
//        private CommandData data;
//        public CommandGameApplyRequest(String roomId) {
//            this.cmd=CMD_APPLY;
//            this.data=new CommandData(roomId);
//        }
//
//        public CommandData getData() {
//            return data;
//        }
//    }
//    //Server-->Client  回复收到预约申请
//    public static class CommandGameApplyResponse extends CommandBase {
//        private CommandData data;
//        public CommandGameApplyResponse(String roomId) {
//            this.cmd=CMD_APPLY_RESPONSE;
//            this.data=new CommandData(roomId);
//        }
//
//        public CommandData getData() {
//            return data;
//        }
//    }
//
//    //================================================================
//    //Client-->Server 取消预约
//    public static class CommandGameCancelApplyRequest extends CommandBase {
//        private CommandData data;
//        public CommandGameCancelApplyRequest(String roomId) {
//            this.cmd=CMD_CANCEL_APPLY;
//            this.data=new CommandData(roomId);
//        }
//
//        public CommandData getData() {
//            return data;
//        }
//    }
//    //Server-->Client 回复收到取消预约指令
//    public static class CommandGameCancelApplyResponse extends CommandBase {
//        private CommandData data;
//        public CommandGameCancelApplyResponse(String roomId) {
//            this.cmd=CMD_CANCEL_APPLY_RESPONSE;
//            this.data=new CommandData(roomId);
//        }
//
//        public CommandData getData() {
//            return data;
//        }
//    }



    //================================================================
    //Client-->Server
    public static class CommandGameConfirmRequestData extends CommandData {
        //protected int confirm;
        protected int gameId;

        @Override
        public boolean isValid() {
            if (!super.isValid())
                return false;
            return true;
        }

        public CommandGameConfirmRequestData(String roomId, int gameId) {
            super(roomId);
            this.gameId = gameId;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }
    }

    public static class CommandGameConfirmRequest extends CommandBase {
        private CommandGameConfirmRequestData data;
        public CommandGameConfirmRequest(String roomId) {
            this.cmd = CMD_GAME_CONFIRM;
            this.data = new CommandGameConfirmRequestData(roomId, 0);
        }

        public CommandGameConfirmRequestData getData() {
            return data;
        }
    }
    //Server-->Client 回复收到确认上机或者放弃玩游戏指令
    public static class CommandGameConfirmResponse extends CommandResponseBase {
        private CommandGameConfirmRequestData data;
        public CommandGameConfirmResponse(String roomId, int gameId) {
            this.cmd = CMD_GAME_CONFIRM_RESPONSE;
            this.data = new CommandGameConfirmRequestData(roomId, gameId);
        }

        public CommandGameConfirmRequestData getData() {
            return data;
        }
    }


    //================================================================
    //Server-->Client 通知奖励
    public static class CommandGameRewardCoinsNotificationData extends CommandData {
        protected GamePlayerInfo player;
        protected int gameId;
        protected int numCoins;

        @Override
        public boolean isValid() {
            if (!super.isValid())
                return false;
            if (player == null || !player.isValid())
                return false;
            return true;
        }

        public CommandGameRewardCoinsNotificationData(String roomId, GamePlayerInfo player, int gameId, int numCoins) {
            super(roomId);
            this.player=player;
            this.gameId=gameId;
            this.numCoins=numCoins;
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

        public int getNumCoins() {
            return numCoins;
        }

        public void setNumCoins(int numCoins) {
            this.numCoins = numCoins;
        }
    }
    public static class CommandGameRewardCoinsNotification extends CommandBase {
        private CommandGameRewardCoinsNotificationData data;
        public CommandGameRewardCoinsNotification(String roomId, GamePlayerInfo player, int gameId, int numCoins) {
            this.cmd=CMD_GAME_REWARD_COINS_NOTIFICATION;
            this.data=new CommandGameRewardCoinsNotificationData(roomId,player,gameId,numCoins);
        }

        public CommandGameRewardCoinsNotificationData getData() {
            return data;
        }
    }
    //Client-->Server 回复收到奖励
//    public static class CommandGameRewardCoinsReply extends CommandBase {
//        private CommandData data;
//        public CommandGameRewardCoinsReply(String roomId) {
//            this.cmd=CMD_GAME_REWARD_COINS_REPLY;
//            this.data=new CommandData(roomId);
//        }
//
//        public CommandData getData() {
//            return data;
//        }
//    }


    //================================================================
    //Client-->Server 投币
    public static class CommandDropCoinsRequest extends CommandBase {
        private CommandData data;
        public CommandDropCoinsRequest(String roomId) {
            this.cmd=CMD_GAME_DROP_COINS;
            this.data=new CommandData(roomId);
        }

        public CommandData getData() {
            return data;
        }
    }
    //Server-->Client 投币结果
    public static class CommandDropCoinsResponseData extends CommandData {
        protected int numCoins;

        @Override
        public boolean isValid() {
            if (!super.isValid())
                return false;
            return true;
        }

        public CommandDropCoinsResponseData(String roomId,int numCoins) {
            super(roomId);
            this.numCoins=numCoins;
        }

        public int getNumCoins() {
            return numCoins;
        }

        public void setNumCoins(int numCoins) {
            this.numCoins = numCoins;
        }
    }
    public static class CommandDropCoinsResponse extends CommandResponseBase {
        private CommandDropCoinsResponseData data;
        public CommandDropCoinsResponse(String roomId, int numCoins) {
            this.cmd=CMD_GAME_DROP_COINS_RESPONSE;
            this.data=new CommandDropCoinsResponseData(roomId, numCoins);
        }

        public CommandDropCoinsResponseData getData() {
            return data;
        }
    }

    public static class CommandGameWiperStop extends CommandBase {
        private CommandData data;
        public CommandGameWiperStop(String roomId) {
            this.cmd=CMD_GAME_WIPER_STOP;
            this.data=new CommandData(roomId);
        }

        public CommandData getData() {
            return data;
        }
    }


    public static class CommandDropTimeoutNotification extends CommandBase {
        private CommandData data;
        public CommandDropTimeoutNotification(String roomId) {
            this.cmd=CMD_GAME_DROP_TIMEOUT_NOTIFICATION;
            this.data=new CommandData(roomId);
        }

        public CommandData getData() {
            return data;
        }
    }

    public static class CommandDropTimeoutReply extends CommandBase {
        private CommandData data;
        public CommandDropTimeoutReply(String roomId) {
            this.cmd=CMD_GAME_DROP_TIMEOUT_REPLY;
            this.data=new CommandData(roomId);
        }

        public CommandData getData() {
            return data;
        }
    }


    public static class CommandGameOverNotificationData extends CommandData {
        public static final int TYPE_TIMEOUT = 0;       // 超时下机
        public static final int TYPE_ROOM_OFFLINE = 1;  // 房间下线

        private int type;
        public CommandGameOverNotificationData(String roomId, int type) {
            super(roomId);
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class CommandGameOverNotification extends CommandBase {
        private CommandGameOverNotificationData data;
        public CommandGameOverNotification(String roomId, int type) {
            this.cmd=CMD_GAME_OVER_NOTIFICATION;
            this.data=new CommandGameOverNotificationData(roomId, type);
        }

        public CommandGameOverNotificationData getData() {
            return data;
        }
    }


    //================================================================
    //Client-->Server
    public static class CommandChatMessageData extends CommandData {
        protected GamePlayerInfo player;
        protected int type; //0,文本消息
        protected String content;

        @Override
        public boolean isValid() {
            if (!super.isValid())
                return false;
            if (player == null || !player.isValid())
                return false;
            if (content == null || content.isEmpty())
                return false;
            return true;
        }

        public CommandChatMessageData(String roomId, GamePlayerInfo player, String content) {
            super(roomId);
            this.player = player;
            this.type = 0;
            this.content = content;
        }

        public GamePlayerInfo getPlayer() {
            return player;
        }

        public void setPlayer(GamePlayerInfo player) {
            this.player = player;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
    public static class CommandChatMessage extends CommandBase {
        private CommandChatMessageData data;
        public CommandChatMessage(String roomId, GamePlayerInfo player, String content) {
            this.cmd=CMD_ROOM_CHAT_MESSAGE;
            data = new CommandChatMessageData(roomId, player, content);
        }

        public CommandChatMessageData getData() {
            return data;
        }

        @Override
        public String toString()
        {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
}


/*
*
*
广播消息
房间信息更新
单播消息
通知上机
出币通知

Request
获取房间信息
预约上机
确认上机
推币
下机(15s超时)

Response
返回房间信息
预约成功/失败
上机成功/失败
推币成功/失败
下机成功/失败

*
* */
