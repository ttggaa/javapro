package com.kariqu.wssrv.app.ws;

import io.netty.channel.ChannelHandlerContext;

public class WsEventActiveClosed extends WsEventBase {
    private String connId;

    public WsEventActiveClosed(String connId, ChannelHandlerContext ctx) {
        super(ctx);
        this.connId = connId;
    }

    public String getConnId() {
        return connId;
    }
}
