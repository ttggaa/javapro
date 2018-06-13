package com.kariqu.wssrv.app.room;

import com.kariqu.wssrv.app.room.pkg.RoomCmd;
import com.kariqu.wssrv.app.tcp.DeviceCmd;
import com.kariqu.wssrv.app.tcp.DeviceEventListener;
import com.kariqu.wssrv.app.tcp.SerialTcpDeviceManager;
import com.kariqu.wssrv.app.transaction.IsTryAgain;
import com.kariqu.wssrv.app.util.SimpleEventDispatcher;
import com.kariqu.wssrv.app.util.Utility;
import com.kariqu.wssrv.app.ws.WsSessionManager;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.entity.Room;
import com.kariqu.zwsrv.thelib.persistance.entity.TbjGame;
import com.kariqu.zwsrv.thelib.persistance.service.AccountLogService;
import com.kariqu.zwsrv.thelib.persistance.service.AccountService;
import com.kariqu.zwsrv.thelib.persistance.service.RoomService;
import com.kariqu.zwsrv.thelib.persistance.service.TbjGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by simon on 10/05/2018.
 */

@Service
public class GameRoomManager extends SimpleEventDispatcher<Object> {
    private static final Logger logger = LoggerFactory.getLogger(GameRoomManager.class);

    static private GameRoomManager sInstance;
    static public GameRoomManager getInstance() {
        return sInstance;
    }

    private AccountService accountService;
    private RoomService roomService;
    private AccountLogService accountLogService;
    private TbjGameService tbjGameService;

    private Map<String, TbjPlayer> playerConnIdMap = new HashMap<>();
    private Map<String, TbjPlayer> playerUserIdMap = new HashMap<>();
    private Map<String, GameRoom> roomIdMap = new HashMap<>();
    private Map<String,GameRoom> deviceIdMap = new ConcurrentHashMap<>(); //deviceId,GameRoom

    public RoomService getRoomService() {
        return roomService;
    }

    @Autowired
    public GameRoomManager(AccountService accountService
            , RoomService roomService
            , AccountLogService accountLogService
            , TbjGameService tbjGameService) {
        super(500);
        SerialTcpDeviceManager.getInstance().addDeviceEventListener(mDeviceEventListener);
        this.accountService = accountService;
        this.roomService = roomService;
        this.accountLogService = accountLogService;
        this.tbjGameService = tbjGameService;

        GameRoomManager.sInstance = this;
        initRoom();
    }

    private void initRoom()
    {
        //String deviceId_100 = "F5278447421824";
        //String deviceId_101 = "F5278421421828";
        List<Room> roomList = roomService.findAll();
        for (Room room : roomList) {
            if (room.getRoomClassify() != Room.ROOM_CLASSIFY_TBJ)
                continue;
            if (room.getDeviceId().isEmpty()) {
                logger.warn("tbj room device_id is null. roomId: {} roomName: {}", room.getRoomId(), room.getName());
                continue;
            }
            String roomIdStr = Integer.toString(room.getRoomId());
            GameRoom gameRoom = new GameRoom(room.getRoomId(), room.getName(), room.getDeviceId(), mRoomNotifyListener, this);
            gameRoom.setRoomCostCoins(room.getRoomCost());
            roomIdMap.put(roomIdStr, gameRoom);
            logger.info("tbj room create success. roomId: {} roomName: {} deviceId: {}"
                    , room.getRoomId(), room.getName(), room.getDeviceId());
        }
    }

    public void notifyConnClosed(String connId) {
        try {
            put(new RoomEventLeaveRoom(connId,null));
        } catch (Exception e) {
            logger.warn("notifyConnClosed exception: {}", e.toString());
        }
    }

    public void notifyJoinRoom(String connId, String jsonString) {
        try {
            put(new RoomEventJoinRoom(connId,jsonString));
        } catch (Exception e) {
            logger.warn("notifyJoinRoom exception: {}", e.toString());
        }
    }

    public void notifyLeaveRoom(String connId, String jsonString) {
        try {
            put(new RoomEventLeaveRoom(connId,jsonString));
        } catch (Exception e) {
            logger.warn("notifyLeaveRoom exception: {}", e.toString());
        }
    }

    public void notifyRoomCmd(String connId, String jsonString) {
        try {
            put(new RoomEventCmd(connId,jsonString));
        } catch (Exception e) {
            logger.warn("notifyRoomCmd exception: {}", e.toString());
        }
    }

    private TbjPlayer findPlayerByConnId(String connId) {
        return playerConnIdMap.get(connId);
    }

    private TbjPlayer findPlayerByUserId(String userId) {
        return playerUserIdMap.get(userId);
    }

    public void playerJoinRoom(TbjPlayer player, GameRoom room) {
        playerConnIdMap.put(player.getConnId(), player);
        playerUserIdMap.put(player.userId(), player);
    }

    public void playerLevelRoom(TbjPlayer player, GameRoom room) {
        playerConnIdMap.remove(player.getConnId());
        playerUserIdMap.remove(player.userId());
    }

    ///
    DeviceEventListener mDeviceEventListener = new DeviceEventListener() {
        @Override
        public void onDropCoins(String deviceId, int coinsNum) {
            put(new CoinsEventDropped(deviceId,coinsNum));
        }

        @Override
        public void onQueryReward(String deviceId, int coinsNum) {
            put(new CoinsEventReward(deviceId,coinsNum));
        }

        @Override
        public void onDropConisConfirm(String deviceId) {
            put(new CoinsEventDropConfirm(deviceId));
        }

        @Override
        public void onQueryDeviceId(String deviceId) {
            put(new RoomEventDeviceId(deviceId));
        }

        @Override
        public void onDeviceOffline(String deviceId) {
            put(new RoomEventOffline(deviceId));
        }

        };


    GameRoom.RoomNotifyListener mRoomNotifyListener = new GameRoom.RoomNotifyListener() {
        @Override
        public void sendToUsers(List<String> connIds, String jsonString) {
            WsSessionManager.getInstance().asyncSendTo(connIds,jsonString);
        }

        @Override
        public void sendToDropCoins(String deviceId) {
            SerialTcpDeviceManager.getInstance().sendToTbjCmd(deviceId, DeviceCmd.CMD_DROP_COINS);
        }

        @Override
        public void sendToDropCoinsConfirm(String deviceId) {
            SerialTcpDeviceManager.getInstance().sendToTbjCmd(deviceId, DeviceCmd.CMD_DROP_COINS_CONFIRM);
        }

        @Override
        public void sendToWiper(String deviceId) {
            SerialTcpDeviceManager.getInstance().sendToTbjCmd(deviceId, DeviceCmd.CMD_WIPER);
        }

        @Override
        public void sendToQueryCoinReward(String deviceId) {
            SerialTcpDeviceManager.getInstance().sendToTbjCmd(deviceId, DeviceCmd.CMD_QUERY_REWARD_COINS);
        }
    };

    @Override
    protected void dispatchOnIdle() {
        for (Map.Entry<String, GameRoom> kv : deviceIdMap.entrySet()) {
            kv.getValue().update();
        }
    }

    @Override
    protected void dispatchOnExit() {

    }

    @Override
    protected void dispatchInWorkThread(Object eventArg) {
        try {
            if (eventArg instanceof RoomEventJoinRoom)
            {
                RoomEventJoinRoom event = (RoomEventJoinRoom)eventArg;
                if (event.getConnId() == null) {
                    logger.warn("connid is null");
                    return;
                }

                // 房间不存在
                String joinRoomId = event.getCmd().getRoomId();
                GameRoom joinRoom = roomIdMap.get(joinRoomId);
                if (joinRoom == null) {
                    logger.warn("can't find room. room_id: {}", joinRoomId);

                    String responseStr = GameRoom.createJoinGameRoomResponse(null, null, 0
                            , joinRoomId, RoomCmd.EC_ROOM_IS_NULL);
                    List<String> connIds = new ArrayList<>();
                    connIds.add(event.getConnId());
                    mRoomNotifyListener.sendToUsers(connIds, Utility.encrypt(responseStr));
                    return;
                }

                // 房间不在线
                if (deviceIdMap.get(joinRoom.getDeviceId()) == null) {
                    logger.warn("room is offline. room_id: {} deviceId: {}", joinRoomId, joinRoom.getDeviceId());
                    String responseStr = GameRoom.createJoinGameRoomResponse(null, null, 0
                            , joinRoomId, RoomCmd.EC_ROOM_OFFLINE);
                    List<String> connIds = new ArrayList<>();
                    connIds.add(event.getConnId());
                    mRoomNotifyListener.sendToUsers(connIds, Utility.encrypt(responseStr));
                    return;
                }

                String connId = event.getConnId();
                TbjPlayer player = findPlayerByConnId(connId);
                if (player == null) {
                    // 新的连接进入房间
                    newConnIdJoinRoom(joinRoom, event);
                } else {
                    // 同一个连接进入房间
                    sameConnIdJoinRoom(joinRoom, event, player);
                }
            }
            else if (eventArg instanceof RoomEventLeaveRoom)
            {
                RoomEventLeaveRoom event = (RoomEventLeaveRoom)eventArg;
                if (event.getConnId() == null)
                    return;
                TbjPlayer player = findPlayerByConnId(event.getConnId());
                if (player == null)
                    return;

                // 玩家强制离开房间
                GameRoom room = player.getRoom();
                room.handlePlayerLeaveBySelf(player);
                //logger.info("test: connIdMap: {} userIdMap: {}", TestMapToString(playerConnIdMap), TestMapToString(playerUserIdMap));
            }
            else if (eventArg instanceof RoomEventCmd)
            {
                RoomEventCmd event = (RoomEventCmd)eventArg;
                if (event.getConnId() == null)
                    return;
                TbjPlayer player = findPlayerByConnId(event.getConnId());
                if (player == null)
                    return;
                player.getRoom().handleRoomCommand(player.userId(), event.getCmd(), player);
            }
            else if (eventArg instanceof CoinsEventDropped) {
                CoinsEventDropped event = (CoinsEventDropped)eventArg;
                GameRoom room = deviceIdMap.get(event.getDeviceId());
                if (room!=null) {
                    room.handleCoinsEventDropped(event);
                }
            }
            else if (eventArg instanceof CoinsEventReward) {
                CoinsEventReward event = (CoinsEventReward)eventArg;
                GameRoom room = deviceIdMap.get(event.getDeviceId());
                if (room!=null) {
                    room.handleCoinsEventReward(event);
                }
            } else if (eventArg instanceof CoinsEventDropConfirm) {
                CoinsEventDropConfirm event = (CoinsEventDropConfirm)eventArg;
                GameRoom room = deviceIdMap.get(event.getDeviceId());
                if (room != null) {
                    room.handleCoinsEventDropConfirm(event);
                }
            } else if (eventArg instanceof RoomEventDeviceId) {
                RoomEventDeviceId event = (RoomEventDeviceId)eventArg;
                boolean succ = false;
                String roomId = "";
                for (Map.Entry<String, GameRoom> kv : roomIdMap.entrySet()) {
                    if (kv.getValue().getDeviceId().endsWith(event.getDeviceId())) {
                        deviceIdMap.put(event.getDeviceId(), kv.getValue());
                        succ = true;
                        roomId = kv.getKey();
                        break;
                    }
                }

                if (!succ) {
                    logger.warn("can't find room deviceId: {}", event.getDeviceId());
                } else {
                    logger.info("update room deviceId success deviceId: {} roomid: {}", event.getDeviceId(), roomId);
                }
            } else if (eventArg instanceof RoomEventOffline) {
                RoomEventOffline event = (RoomEventOffline)eventArg;
                String deviceId = event.getDeviceId();

                for (Map.Entry<String, GameRoom> kv : roomIdMap.entrySet()) {
                    if (kv.getValue().getDeviceId().endsWith(deviceId)) {
                        GameRoom room = kv.getValue();
                        room.handleRoomOffline();
                        break;
                    }
                }
                deviceIdMap.remove(deviceId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("exception {}", e.toString());
        }
    }

    private void newConnIdJoinRoom(GameRoom joinRoom, RoomEventJoinRoom event) {
        String newConnId = event.getConnId();
        String userId = event.getCmd().getPlayerInfo().getUserID();
        String joinRoomId = joinRoom.getRoomId();
        TbjPlayer player = findPlayerByUserId(userId);

        // 此用户已经在某个房间里
        if (player != null) {
            // 1.如果房间里的用户还处于连接状态，断开此连接
            String oldConnId = player.getConnId();
            if (!oldConnId.isEmpty()) {
                WsSessionManager.getInstance().asyncCloseAppChannel(oldConnId);
                logger.info("user repeated join. uid: {} close old connId: {} new connId: {} "
                        , userId, oldConnId, newConnId);

                // 删除旧的connId->player
                playerConnIdMap.remove(oldConnId);
            }
            player.setConnId(newConnId);
            // 设置新的connId->player
            playerConnIdMap.put(newConnId, player);

            // 2.当前所在房间就是要加入的房间
            GameRoom currentRoom = player.getRoom();
            if (currentRoom.getRoomId().equals(joinRoomId)) {
                logger.info("user repeated join. currentRoomId == joinRoomId uid: {} roomId: {} connId: {} "
                        , userId, joinRoomId, player.getConnId());
                currentRoom.handlePlayerRelogin(player);
            } else {
                logger.info("user repeated join. currentRoomId != joinRoomId uid: {} cRoomId: {} jRoomId: {}  connId: {} "
                        , userId, currentRoom.getRoomId(), joinRoomId, player.getConnId());

                // 3.当前所在房间不是要加入的房间
                // 3.1离开当前房间
                currentRoom.handlePlayerLeaveBySystem(player);

                // 3.2进入需要加入的房间
                joinRoom.handleRoomCommand(player.userId(), event.getCmd(), player);
            }
        } else {
            // 用户不存在
            TbjPlayer newPlayer = new TbjPlayer(newConnId, event.getCmd().getPlayerInfo());
            joinRoom.handleRoomCommand(newPlayer.userId(), event.getCmd(), newPlayer);
        }

        //logger.info("test: connIdMap: {} userIdMap: {}", TestMapToString(playerConnIdMap), TestMapToString(playerUserIdMap));
    }

    private void sameConnIdJoinRoom(GameRoom joinRoom, RoomEventJoinRoom event, TbjPlayer player) {
        String connId = event.getConnId();
        String joinRoomId = joinRoom.getRoomId();
        String userId = player.userId();

        GameRoom currentRoom = player.getRoom();
        // 不应该出现这种情况！！
        if (currentRoom == null) {
            logger.warn("player current room is null! uid: {} joinRoomId: {} connId: {}", userId, joinRoomId, connId);
            return;
        }

        // 已经在room中, 不允许多次进入相同room
        if (currentRoom.getRoomId().equals(joinRoomId)) {
            logger.warn("same connId repeated join. currentRoom == JoinRoom. uid: {} room_id: {} conn_id:{} "
                    , userId, joinRoomId, connId);
            currentRoom.handlePlayerRelogin(player);
        } else {
            // 已经在room中，要加入别的room
            logger.warn("save connId repeated join. currentRoom != JoinRoom. uid: {} cRoomId: {} jRoomId: {} conn_id:{} "
                    , userId, currentRoom.getRoomId(), joinRoomId, connId);
            currentRoom.handlePlayerLeaveBySystem(player);
            joinRoom.handleRoomCommand(userId, event.getCmd(), player);
        }
        //logger.info("test: connIdMap: {} userIdMap: {}", TestMapToString(playerConnIdMap), TestMapToString(playerUserIdMap));
    }

    private static String TestMapToString(Map<String, TbjPlayer> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, TbjPlayer> kv : map.entrySet()) {
            String s = String.format("{%s:%s}", kv.getKey(), kv.getValue().userId());
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    public Account playerAccount(String userIdStr) {
        int userId = Utility.ParseUserIdToInt(userIdStr);
        if (userId == -1)
            return null;
        return accountService.findOne(userId);
    }

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor = {Exception.class})
    public Account playerAddCoin(String userIdStr, int num, GameRoom room) {
        int userId = Utility.ParseUserIdToInt(userIdStr);
        if (userId == -1)
            return null;

        Account account = accountService.findOne(userId);
        if (account == null)
            return null;
        int v = account.getCoins() + num;
        account.setCoins(v);
        account.setAvailableCoins(v);
        //accountLogService.saveTbjCoinsChange(userId, num, AccountLogService.CHANGE_TYPE_COINS_TBJ_REWARD);
        return accountService.save(account);
    }

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor = {Exception.class})
    public Account accountReduceCoin(Account account, int num) {
        int n = account.getCoins() - num;
        if (n < 0)
            n = 0;
        account.setCoins(n);
        account.setAvailableCoins(n);
        //accountLogService.saveTbjCoinsChange(account.getUserId(), -num, AccountLogService.CHANGE_TYPE_COINS_TBJ_DROP);
        return accountService.save(account);
    }

    // 玩家上机
    public void playerGameConfirm(CurrentPlayer currentPlayer, GameRoom room) {
        TbjGame game = new TbjGame();
        game.setRoomId(room.getRoomIdInt());
        game.setRoomName(room.getRoomName());
        game.setDeviceId(room.getDeviceId());
        game.setRoomCost(room.getRoomCostCoins());
        game.setUserId(Utility.ParseUserIdToInt(currentPlayer.userId()));
        game.setStartTime(new Date());
        game = tbjGameService.save(game);
        currentPlayer.setGame(game);
    }

    // 玩家下机
    public void playerXiaJi(CurrentPlayer currentPlayer, GameRoom room) {
        TbjGame game = currentPlayer.getGame();
        if (game == null)
            return;
        game.setEndTime(new Date());
        game.setDropCount(currentPlayer.getDropCount());
        game.setDropCoins(currentPlayer.getDropCoins());
        game.setRewardCount(currentPlayer.getRewardCount());
        game.setRewardCoins(currentPlayer.getRewardCoins());
        game.setCoinsDelta(game.getRewardCoins() - game.getDropCoins());
        tbjGameService.save(game);

        int userId = Utility.ParseUserIdToInt(currentPlayer.userId());
        if (userId == -1) {
            logger.warn("can't parse userId. uid: {} roomId: {}", currentPlayer.userId(), room.getRoomIdInt());
            return;
        }

        // 更新玩家的娃娃币账单
        accountLogService.saveTbjCoinsChange(userId, game.getCoinsDelta(), AccountLogService.CHANGE_TYPE_COINS_TBJ_DROP);
    }
}