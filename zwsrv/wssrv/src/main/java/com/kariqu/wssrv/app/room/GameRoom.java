package com.kariqu.wssrv.app.room;

import com.kariqu.wssrv.app.room.pkg.GamePlayerInfo;
import com.kariqu.wssrv.app.room.pkg.GameRoomStatusDetailInfo;
import com.kariqu.wssrv.app.room.pkg.RoomCmd;
import com.kariqu.wssrv.app.util.JsonUtil;
import com.kariqu.wssrv.app.util.Utility;
import com.kariqu.wssrv.app.ws.WsCmdHead;
import com.kariqu.wssrv.app.ws.WsCmdType;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.entity.Room;
import com.kariqu.zwsrv.thelib.persistance.entity.TbjGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 11/05/2018.
 */
public class GameRoom {
    private static final Logger logger = LoggerFactory.getLogger(GameRoom.class);

    private static final long QUERY_REWARD_MILLISECONDS = 2000;
    private static final long DROP_TIMEOUT_MILLISECONDS = 40 * 1000;

    public interface RoomNotifyListener {
        void sendToUsers(List<String> connIds, String jsonString);

        // 投币
        void sendToDropCoins(String deviceId);

        // 出币
        void sendToDropCoinsConfirm(String deviceId);

        // 雨刷
        void sendToWiper(String deviceId);

        // 查询奖励
        void sendToQueryCoinReward(String deviceId);
    }

    private final int roomIdInt;
    private final String roomId;
    private final String roomName;
    private final String deviceId;
    private final RoomNotifyListener listener;
    private GameRoomManager mgr;
    private long queryRewardTimestamp;
    private int roomCostCoins;

    private Map<String, TbjPlayer> totalPlayerUserIdMap = new HashMap<>();

    private CurrentPlayer currentPlayer;        // 已经上机的玩家

    public int gameId() {
        if (currentPlayer != null) {
            TbjGame game = currentPlayer.getGame();
            if (game != null)
                return game.getGameId();
        }
        return 0;
    }

    public int getRoomIdInt() {
        return roomIdInt;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getRoomCostCoins() {
        return roomCostCoins;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomCostCoins(int roomCostCoins) {
        this.roomCostCoins = roomCostCoins;
    }

    public GameRoom(int roomIdInt, String roomName, String deviceId, RoomNotifyListener listener, GameRoomManager mgr) {
        this.roomIdInt = roomIdInt;
        this.roomName = roomName;
        this.roomId = Integer.toString(roomIdInt);
        this.deviceId=deviceId!=null?deviceId:"";
        this.listener=listener;
        this.mgr = mgr;
        this.queryRewardTimestamp = System.currentTimeMillis();
        this.roomCostCoins = 1;
    }

    private List<GamePlayerInfo> getAllGamePlayerInfo() {
        List<GamePlayerInfo> list = new ArrayList<>();
        for (Map.Entry<String, TbjPlayer> kv : totalPlayerUserIdMap.entrySet()) {
            list.add(kv.getValue().getPlayerInfo());
        }
        return list;
    }

    private List<String> getAllConnIds() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, TbjPlayer> kv : totalPlayerUserIdMap.entrySet()) {
            list.add(kv.getValue().getConnId());
        }
        return list;
    }

    private List<String> getAllConnIdsExcept(String exceptConnId) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, TbjPlayer> kv : totalPlayerUserIdMap.entrySet()) {
            String connId = kv.getValue().getConnId();
            if (exceptConnId.equals(connId))
                continue;
            list.add(connId);
        }
        return list;
    }

    // 轮询
    public void update() {
        // 定时查询奖励
        long tnow = System.currentTimeMillis();
        updateQueryReward(tnow);

        // 定时检查上机玩家是否超时
        if (currentPlayer != null)
            updateDropTimeout(tnow);
    }

    private void updateQueryReward(long tnow) {
        // 每2秒去查询
        if (tnow - queryRewardTimestamp > QUERY_REWARD_MILLISECONDS) {
            queryRewardTimestamp = tnow;
            listener.sendToQueryCoinReward(deviceId);

            if (roomIdInt == 101) {
                //logger.info("-----------------> query reward roomId: {} devId: {}", roomIdInt, deviceId);
            }
        }
    }

    private void updateDropTimeout(long tnow) {
        if (tnow - currentPlayer.getDropTimestamp() > DROP_TIMEOUT_MILLISECONDS) {
            logger.info("current player timeout kickout room. uid: {} roomId: {} gameId: {}"
                    , currentPlayer.userId(), roomId, gameId());

            TbjPlayer player = currentPlayer.getPlayer();
            // 超时，强制离开房间
            playerForceLeaveRoom(player);

            RoomCmd.CommandGameOverNotification response = new RoomCmd.CommandGameOverNotification(roomId
                    , RoomCmd.CommandGameOverNotificationData.TYPE_TIMEOUT);
            sendToApp(player.getConnId(), response);
        }
    }

    private void playerForceLeaveRoom(TbjPlayer player) {
        String userId = player.userId();

        // 玩家正在上机,先下机
        if (currentPlayer != null && currentPlayer.userId().equals(userId)) {
            currentPlayerXiaJi();
        }
        player.setRoom(null);
        totalPlayerUserIdMap.remove(userId);
        notifyRoomChangedToAll();
        mgr.playerLevelRoom(player, this);
    }

    private void currentPlayerXiaJi() {
        mgr.playerXiaJi(currentPlayer, this);
        currentPlayer = null;
    }

    // 收到推币机返回的投币消息
    public void handleCoinsEventDropped(CoinsEventDropped event) {
        if (currentPlayer == null) {
            logger.warn("currentPlayer is null. roomId: {}", roomId);
            return;
        }

        // 发送出币命令
        logger.info("sendTo Tbj start confirm uid: {} roomId: {}", currentPlayer.userId(), roomId);
        listener.sendToDropCoinsConfirm(deviceId);
    }

    // 收到推币机返回的出币消息
    public void handleCoinsEventDropConfirm(CoinsEventDropConfirm event) {
        logger.info("room received Tbj tDropConfirm roomId: {}", roomId);
        if (currentPlayer == null) {
            logger.warn("currentPlayer is null. roomId: {}", roomId);
            return;
        }

        // 返回雨刷消息或者是出币消息
    }

    // 查询奖励返回
    public void handleCoinsEventReward(CoinsEventReward event) {
        int num = event.getNumCoins();
        //logger.info("received reward coins: {}", num);
        if (num <= 0)
            return;

        if (currentPlayer == null) {
            logger.warn("reward coins: {} but currentPlayer is null. roomId: {} ", num, roomId);
            return;
        }

        // 收到奖励时，重置时间
        currentPlayer.setDropTimestamp(System.currentTimeMillis());

        int totalNum = num * getRoomCostCoins();
        Account account = mgr.playerAddCoin(currentPlayer.userId(), totalNum, this);
        if (account == null) {
            logger.info("account is null. add coins: {} uid: {} roomId: {} odds: {}"
                    , totalNum, currentPlayer.userId(), roomId, getRoomCostCoins());
        } else {
            RoomCmd.CommandGameRewardCoinsNotification response
                    = new RoomCmd.CommandGameRewardCoinsNotification(roomId, currentPlayer.getPlayer().getPlayerInfo()
                    , gameId(), account.getCoins());
            sendToApp(currentPlayer.getPlayer().getConnId(), response);
            logger.info("user add coins success. coins: {} uid: {} roomId: {} odds: {} account: {}"
                    , totalNum, currentPlayer.userId(), roomId, getRoomCostCoins(), account.getCoins());

            currentPlayer.onUpdateReward(totalNum);
        }

        logger.info("reward return roomId: {} devId: {} num: {}", roomIdInt, deviceId, num);
    }

    public void handleRoomCommand(String userId, Object cmdArg, TbjPlayer player) {
        if (cmdArg instanceof RoomCmd.CommandJoinGameRoomRequest) {
            handleCommandJoinGameRoomRequest(player, (RoomCmd.CommandJoinGameRoomRequest)cmdArg);
        }
        else if (cmdArg instanceof RoomCmd.CommandGetGameRoomInfoRequest) {
            handleCommandGetGameRoomInfoRequest(userId,(RoomCmd.CommandGetGameRoomInfoRequest)cmdArg);
        }
        else if (cmdArg instanceof RoomCmd.CommandGameConfirmRequest) {
            handleCommandGameConfirmRequest(player.getConnId(), userId, (RoomCmd.CommandGameConfirmRequest) cmdArg);
        }
        else if (cmdArg instanceof RoomCmd.CommandDropCoinsRequest) {
            handleCommandDropCoinsRequest(player.getConnId(), userId, (RoomCmd.CommandDropCoinsRequest) cmdArg);
        }
        else if (cmdArg instanceof RoomCmd.CommandChatMessage) {
            handleCommandChatMessage(userId,(RoomCmd.CommandChatMessage)cmdArg);
        }
        else if (cmdArg instanceof RoomCmd.CommandDropTimeoutNotification) {
            handleCommandDropTimeoutNotification(userId, (RoomCmd.CommandDropTimeoutNotification)cmdArg);
        }
        else if (cmdArg instanceof RoomCmd.CommandGameWiperStop) {
            handleCommandGameWiperStop(userId, (RoomCmd.CommandGameWiperStop)cmdArg);
        }
    }

    public void handlePlayerRelogin(TbjPlayer player) {
        logger.info("player relogin roomid: {} user_id: {}", roomId, player.userId());

        GamePlayerInfo currentPlayerInfo = currentPlayer != null ? currentPlayer.getPlayer().getPlayerInfo() : null;
        GameRoomStatusDetailInfo info = new GameRoomStatusDetailInfo(getAllGamePlayerInfo()
                , currentPlayerInfo, gameId());
        RoomCmd.CommandJoinGameRoomResponse response = new RoomCmd.CommandJoinGameRoomResponse(this.roomId, info);
        WsCmdHead wsCmd = new WsCmdHead();
        wsCmd.setType(WsCmdType.RSP_TBJ_JOIN_ROOM.getType());
        wsCmd.setContent(JsonUtil.convertObject2Json(response));
        String jsonString = JsonUtil.convertObject2Json(wsCmd);

        List<String> connIds = new ArrayList<>();
        connIds.add(player.getConnId());
        listener.sendToUsers(connIds, Utility.encrypt(jsonString));
    }

    public void handlePlayerLeaveBySelf(TbjPlayer player) {
        playerForceLeaveRoom(player);
        logger.info("player force leave room. uid: {} roomId: {}", player.userId(), roomId);
    }

    public void handlePlayerLeaveBySystem(TbjPlayer player) {
        playerForceLeaveRoom(player);
        logger.info("player force leave room by system. uid: {} roomId: {}", player.userId(), roomId);
    }

    public void handleRoomOffline() {
        logger.info("room offline.  deviceId: {} roomid: {}", getDeviceId(), roomId);

        // 当前有玩家，下机
        if (currentPlayer != null)
            currentPlayerXiaJi();

        for (Map.Entry<String, TbjPlayer> kv : totalPlayerUserIdMap.entrySet()) {
            TbjPlayer player = kv.getValue();
            player.setRoom(this);
            mgr.playerLevelRoom(player, this);
        }
        RoomCmd.CommandGameOverNotification response = new RoomCmd.CommandGameOverNotification(roomId
                , RoomCmd.CommandGameOverNotificationData.TYPE_ROOM_OFFLINE);
        sendToApp(getAllConnIds(), response);
    }

    public static String createJoinGameRoomResponse(List<GamePlayerInfo> all, GamePlayerInfo current, int gameId, String roomId, int ec) {
        GameRoomStatusDetailInfo info = new GameRoomStatusDetailInfo(all, current, gameId);
        RoomCmd.CommandJoinGameRoomResponse response = new RoomCmd.CommandJoinGameRoomResponse(roomId, info);
        response.setErrorCode(ec);
        WsCmdHead wsCmd = new WsCmdHead();
        wsCmd.setType(WsCmdType.RSP_TBJ_JOIN_ROOM.getType());
        wsCmd.setContent(JsonUtil.convertObject2Json(response));
        String jsonString = JsonUtil.convertObject2Json(wsCmd);
        return jsonString;
    }

    private void handleCommandJoinGameRoomRequest(TbjPlayer player, RoomCmd.CommandJoinGameRoomRequest cmd) {
        logger.info("roomid: {} user_id: {}", roomId, player.userId());

        totalPlayerUserIdMap.put(player.userId(), player);
        player.setRoom(this);

        GamePlayerInfo currentPlayerInfo = currentPlayer != null ? currentPlayer.getPlayer().getPlayerInfo() : null;
        String responseStr = createJoinGameRoomResponse(getAllGamePlayerInfo(), currentPlayerInfo, gameId(), roomId, RoomCmd.EC_SUCCESS);
        List<String> connIds = new ArrayList<>();
        connIds.add(player.getConnId());
        listener.sendToUsers(connIds, Utility.encrypt(responseStr));
        mgr.playerJoinRoom(player, this);

        // 更新场景
        notifyRoomChangedToAll();
    }

    private void handleCommandGetGameRoomInfoRequest(String userId, RoomCmd.CommandGetGameRoomInfoRequest cmd) {
        logger.info("roomid: {} user_id: {}", roomId, userId);

        GamePlayerInfo currentPlayerInfo = currentPlayer != null ? currentPlayer.getPlayer().getPlayerInfo() : null;
        GameRoomStatusDetailInfo detailInfo = new GameRoomStatusDetailInfo(getAllGamePlayerInfo()
                , currentPlayerInfo, gameId());

        RoomCmd.CommandGetGameRoomInfoResponse response = new RoomCmd.CommandGetGameRoomInfoResponse(roomId, detailInfo);
        TbjPlayer player = totalPlayerUserIdMap.get(userId);
        if (player == null) {
            response.setErrorCode(RoomCmd.EC_PLAYER_NOT_IN_ROOM);
        }
        sendToApp(player.getConnId(), new RoomCmd.CommandGetGameRoomInfoResponse(roomId, detailInfo));
    }

    private void handleCommandGameConfirmRequest(String connId, String userId, RoomCmd.CommandGameConfirmRequest cmd) {
        RoomCmd.CommandGameConfirmResponse response = new RoomCmd.CommandGameConfirmResponse(getRoomId(), gameId());
        TbjPlayer player = totalPlayerUserIdMap.get(userId);
        if (player == null) {
            response.setErrorCode(RoomCmd.EC_PLAYER_NOT_IN_ROOM);
            sendToApp(connId, response);
            return;
        }
        if (currentPlayer != null) {
            response.setErrorCode(RoomCmd.EC_OTHER_PLAYER_IN_GAME);
            sendToApp(player.getConnId(), response);
            return;
        }

        // 每次上机时，获取当前房间的roomCost,如果获取失败，用之前的
        Room room = mgr.getRoomService().findOne(roomIdInt);
        if (room == null) {
            logger.warn("find zww_room is null. uid: {} roomid: {}", player.userId(), roomIdInt);
        } else {
            this.roomCostCoins = room.getRoomCost();
        }

        logger.info("roomid: {} user_id: {} roomCost: {}", roomId, player.userId(), getRoomCostCoins());

        // 判断金币是不是足够
        Account accout = mgr.playerAccount(player.userId());
        if (accout == null || accout.getCoins() < getRoomCostCoins()) {
            // 金币不足上机失败
            response.setErrorCode(RoomCmd.EC_IN_GAME_COIN_INSUFFICIENT);
            sendToApp(player.getConnId(), response);

            logger.warn("coins insufficient game cancel. room_id: {} user_id: {} game_id: {} odds: {}"
                    , roomId, player.userId(), gameId(), getRoomCostCoins());
            return;
        }

        sendToApp(player.getConnId(), response);
        currentPlayer = new CurrentPlayer(player);
        mgr.playerGameConfirm(currentPlayer, this);

        notifyRoomChangedToAll();
        logger.info("game start. room_id: {} user_id: {} game_id: {}"
                , roomId, player.userId(), gameId());
    }

    private void handleCommandDropCoinsRequest(String connId, String userId, RoomCmd.CommandDropCoinsRequest cmd) {
        RoomCmd.CommandDropCoinsResponse response = new RoomCmd.CommandDropCoinsResponse(getRoomId(), getRoomCostCoins());
        TbjPlayer player = totalPlayerUserIdMap.get(userId);
        if (player == null) {
            response.setErrorCode(RoomCmd.EC_PLAYER_NOT_IN_ROOM);
            sendToApp(connId, response);
            return;
        }

        if (currentPlayer == null || !currentPlayer.userId().equals(player.userId())) {
            response.setErrorCode(RoomCmd.EC_PLAYER_NOT_IN_GAME);
            sendToApp(player.getConnId(), response);
            return;
        }


        // 判断用户的金币是否足够
        Account account = mgr.playerAccount(currentPlayer.userId());
        if (account == null || account.getCoins() < getRoomCostCoins()) {
            response.setErrorCode(RoomCmd.EC_DROP_COIN_INSUFFICIENT);
            sendToApp(player.getConnId(), response);
            return;
        }

        logger.info("roomid: {} user_id: {} odd: {} beforeCoin: {}", roomId, player.userId(), getRoomCostCoins(), account.getCoins());

        // TODO 扣除金币 log
        account = mgr.accountReduceCoin(account, getRoomCostCoins());
        response.getData().setNumCoins(account.getCoins());

        sendToApp(player.getConnId(), response);
        currentPlayer.setDropTimestamp(System.currentTimeMillis());
        currentPlayer.onDropCoin(getRoomCostCoins());

        // 发送给推币机
        listener.sendToDropCoins(deviceId);
    }

    private void handleCommandChatMessage(String userId, RoomCmd.CommandChatMessage cmd) {
        TbjPlayer player = totalPlayerUserIdMap.get(userId);
        if (player == null)
            return;

        logger.info("roomid: {} user_id: {}", roomId, player.userId());
        List<String> connIds = getAllConnIdsExcept(player.getConnId());
        sendToApp(connIds, cmd);
    }

    private void handleCommandDropTimeoutNotification(String userId, RoomCmd.CommandDropTimeoutNotification cmd) {
        TbjPlayer player = totalPlayerUserIdMap.get(userId);
        if (player == null)
            return;
        if (currentPlayer == null)
            return;
        if (!currentPlayer.userId().equals(userId)) {
            return;
        }

        // 当前玩家下机
        currentPlayerXiaJi();

        notifyRoomChangedToAll();
        sendToApp(player.getConnId(), new RoomCmd.CommandDropTimeoutReply(roomId));
    }

    private void handleCommandGameWiperStop(String userId, RoomCmd.CommandGameWiperStop cmd) {
        TbjPlayer player = totalPlayerUserIdMap.get(userId);
        if (player == null)
            return;
        if (currentPlayer == null)
            return;
        if (!currentPlayer.userId().equals(userId))
            return;
        listener.sendToWiper(deviceId);
    }

    private void notifyRoomChangedToAll() {
        List<String> connIds = getAllConnIds();
        GamePlayerInfo currentPlayerInfo = currentPlayer != null ? currentPlayer.getPlayer().getPlayerInfo() : null;
        GameRoomStatusDetailInfo detailInfo = new GameRoomStatusDetailInfo(getAllGamePlayerInfo()
                , currentPlayerInfo, gameId());
        sendToApp(connIds, new RoomCmd.CommandGameRoomUpdateNotification(roomId, detailInfo));
    }

    private void sendToApp(String connId, RoomCmd.CommandBase cmdBase) {
        List<String> connIds = new ArrayList<>();
        connIds.add(connId);
        listener.sendToUsers(connIds, Utility.createTbjCmdJsonString(cmdBase));
    }

    private void sendToApp(List<String> connIds, RoomCmd.CommandBase cmdBase) {
        listener.sendToUsers(connIds, Utility.createTbjCmdJsonString(cmdBase));
    }
}
