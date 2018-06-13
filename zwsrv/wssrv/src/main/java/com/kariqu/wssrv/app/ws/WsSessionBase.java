package com.kariqu.wssrv.app.ws;

import com.kariqu.wssrv.app.error.ErrorCode;
import com.kariqu.wssrv.app.util.AESCoder;
import com.kariqu.wssrv.app.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by simon on 10/05/2018.
 */
public abstract class WsSessionBase {

    private static final Logger logger = LoggerFactory.getLogger(WsSessionBase.class);

    public static final byte[] cmdSecretKey = "8e36246eeb6a43e7".getBytes();

    protected String connId;
    protected ChannelHandlerContext ctx;

    public WsSessionBase(long connId, ChannelHandlerContext ctx) {
        this.connId = String.valueOf(connId);
        this.ctx = ctx;
    }

    public String getConnId() {
        return connId;
    }

    public ChannelId channelId() {
        return ctx.channel().id();
    }

    public void handleWillRemoved() {

    }

    public void handleEventData(WsEventCmd cmd) {
        int type = cmd.getCmdHead().getType();
        WsCmdType wsCmdType = WsCmdType.valueOf(type);
        if (wsCmdType!=null) {
            switch (wsCmdType) {
                case REQ_LOGIN:             reqLogin(cmd); return;
                case REQ_BROADCAST_ALL_APP: reqBroadcastAllApp(cmd); return;
                case REQ_ROUTER:            reqRouter(cmd); return;
                default:
                    break;
            }
            logger.warn("unknwon req {} {}", wsCmdType.getType(), wsCmdType.getName());
        } else {
            logger.warn("unknwon type: {}", type);
        }
    }

    protected void reqLogin(WsEventCmd cmd) {

    }

    protected void reqBroadcastAllApp(WsEventCmd cmd) {

    }

    protected void reqRouter(WsEventCmd cmd) {

    }

    public void shutdownChannel(CloseFramer closeFramer) {
        Channel channel = ctx.channel();
        channel.writeAndFlush(closeFramer);
        ChannelFuture f2 = ctx.close();
    }

    public void sendFinallyMessage(String content)  {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(content));
    }

    public void sendResponse(WsCmdType type) {
        sendResponse(new WsCmdResponse(type));
    }
    public void sendResponse(WsCmdType type, ErrorCode.ErrorResult result) {
        sendResponse(new WsCmdResponse(type, result));
    }
    public void sendResponse(WsCmdResponse response)  {

        String jsonString = JsonUtil.convertObject2Json(response);
        try {
            String jsonStringEncrypt = AESCoder.encrypt(WsSessionBase.cmdSecretKey, jsonString);
            if (jsonStringEncrypt!=null&&jsonStringEncrypt.length()>0) {
                sendFinallyMessage(jsonStringEncrypt);
            }
        } catch (Exception e) {
            logger.warn("send json message encrypt error {}", jsonString);
            return;
        }
    }
}
