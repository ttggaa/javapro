package com.kariqu.wssrv.app.ws;

import com.kariqu.wssrv.app.room.GameRoomManager;
import com.kariqu.wssrv.app.util.AESCoder;
import com.kariqu.wssrv.app.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by simon on 10/05/2018.
 */
public class WsSessionApp extends WsSessionBase {

    private static final Logger logger = LoggerFactory.getLogger(WsSessionApp.class);

    public WsSessionApp(long connId, ChannelHandlerContext ctx) {
        super(connId, ctx);
    }

    @Override
    public void handleEventData(WsEventCmd cmd) {
        int type = cmd.getCmdHead().getType();
        WsCmdType wsCmdType = WsCmdType.valueOf(type);
        if (wsCmdType!=null) {
            if (wsCmdType==WsCmdType.REQ_TBJ_JOIN_ROOM) {
                handleTbjJoinRoom(cmd);
            } else if (wsCmdType==WsCmdType.REQ_TBJ_LEAVE_ROOM) {
                handleTbjLeaveRoom(cmd);
            } else if (wsCmdType==WsCmdType.REQ_TBJ_CMD) {
                handleTbjCmd(cmd);
            } else {
                super.handleEventData(cmd);
            }
        } else {
            logger.warn("unknwon type: {}", type);
        }
    }

    @Override
    protected void reqRouter(WsEventCmd msg) {
        //logger.info("app reqRouter connId: {} {}", connId, msg.getJsonString());
        List<String> toList = msg.getCmdHead().getTo();
        msg.getCmdHead().setConnid(getConnId());
        if (toList!=null&&!toList.isEmpty()) {
            String jsonString = JsonUtil.convertObject2Json(msg.getCmdHead());
            if (jsonString!=null&&jsonString.length()>0) {
                try {
                    String jsonStringEncrypt = AESCoder.encrypt(WsSessionBase.cmdSecretKey, jsonString);
                    if (jsonStringEncrypt!=null&&jsonStringEncrypt.length()>0) {
                        WsSessionManager.getInstance().syncSendToWithSrvUserID(toList.get(0), jsonStringEncrypt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("routerApp json string encrypt error {}", jsonString);
                }
            }
        }
    }

    @Override
    public void handleWillRemoved() {
        GameRoomManager.getInstance().notifyConnClosed(getConnId());
    }

    private void urldecodeCmd(WsEventCmd cmd) {
        try {
            String str = URLDecoder.decode(cmd.getCmdHead().getContent(), "utf-8");
            cmd.getCmdHead().setContent(str);
        } catch (UnsupportedEncodingException e) {
            logger.warn("urldecode exception: {} content: {}", e.toString(), cmd.getCmdHead().getContent());
        }
    }

    private void handleTbjJoinRoom(WsEventCmd cmd) {
        urldecodeCmd(cmd);
        logger.info("handleTbjJoinRoom cmd: {}", cmd.getCmdHead().getType());
        GameRoomManager.getInstance().notifyJoinRoom(getConnId(), cmd.getCmdHead().getContent());
    }

    private void handleTbjLeaveRoom(WsEventCmd cmd) {
        urldecodeCmd(cmd);
        logger.info("handleTbjLeaveRoom cmd: {}", cmd.getCmdHead().getType());
        GameRoomManager.getInstance().notifyLeaveRoom(getConnId(), cmd.getCmdHead().getContent());
    }

    private void handleTbjCmd(WsEventCmd cmd) {
        urldecodeCmd(cmd);
        logger.info("handleTbjCmd cmd: {}", cmd.getCmdHead().getType());
        GameRoomManager.getInstance().notifyRoomCmd(getConnId(), cmd.getCmdHead().getContent());
    }
}
