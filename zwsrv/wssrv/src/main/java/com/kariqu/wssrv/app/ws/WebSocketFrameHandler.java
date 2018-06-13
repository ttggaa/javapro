/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.kariqu.wssrv.app.ws;

import com.google.gson.Gson;
import com.kariqu.wssrv.app.util.AESCoder;
import com.kariqu.wssrv.app.util.JsonUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    private final WsSessionType sessionType;

    public WebSocketFrameHandler(WsSessionType sessionType) {
        this.sessionType=sessionType;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelActive channel_id: {}", ctx.channel().id().asShortText());
        WsSessionManager.getInstance().put(new WsEventAccept(ctx, sessionType));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive channel_id: {}", ctx.channel().id().asShortText());
        WsSessionManager.getInstance().put(new WsEventClosed(ctx));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    logger.info("reader idle. {}", ctx.channel().id().asShortText());
                    closeChannel(ctx);
                    break;
                case WRITER_IDLE:
                    logger.info("writer idle. {}", ctx.channel().id().asShortText());
                    closeChannel(ctx);
                    break;
                case ALL_IDLE:
                    logger.info("all idle. {}", ctx.channel().id().asShortText());
                    closeChannel(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled
        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String jsonStringEncrypt = ((TextWebSocketFrame) frame).text();
            String jsonString = "";
            // 解密
            try {
                jsonString = AESCoder.decrypt(WsSessionBase.cmdSecretKey,jsonStringEncrypt);
            } catch (Exception e) {
                Channel channel = ctx.channel();
                String str =  String.format("decode websocket request decrypt error. shutdownChannel channel: %s %s"
                        , e.toString(), channel.id().asShortText());
                throw new UnsupportedOperationException(str);
            }
            Gson gson = new Gson();
            //WsCmdHead cmdHead = gson.fromJson(jsonString, WsCmdHead.class);
            WsCmdHead cmdHead = JsonUtil.convertJson2Model(jsonString,WsCmdHead.class);
            if (cmdHead==null) {
                Channel channel = ctx.channel();
                String str =  String.format("decode websocket error. shutdownChannel channel: %s", channel.id().asShortText());
                throw new UnsupportedOperationException(str);
            }
            WsSessionManager.getInstance().put(new WsEventCmd(ctx, cmdHead, jsonString));
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.warn("exception: {}", cause.toString());
        closeChannel(ctx);
    }

    void closeChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channel.writeAndFlush(CloseFramer.normalClose());
        ChannelFuture f2 = ctx.close();
        f2.addListener(
                new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) {
                        logger.info("exit operationComplete channel_id: {}", ctx.channel().id().asShortText());
                        WsSessionManager.getInstance().put(new WsEventClosed(ctx));
                    }
                }
        );
    }
}
