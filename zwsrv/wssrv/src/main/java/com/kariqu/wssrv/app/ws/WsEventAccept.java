package com.kariqu.wssrv.app.ws;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 10/05/2018.
 */
public class WsEventAccept extends WsEventBase {

    private final WsSessionType sessionType;

    public WsEventAccept(ChannelHandlerContext ctx, WsSessionType sessionType) {
        super(ctx);
        this.sessionType=sessionType;
    }

    public WsSessionType getSessionType() {
        return sessionType;
    }

}
