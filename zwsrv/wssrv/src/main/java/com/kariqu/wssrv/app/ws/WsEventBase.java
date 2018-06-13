package com.kariqu.wssrv.app.ws;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 10/05/2018.
 */
public class WsEventBase {
    private final ChannelHandlerContext ctx;

    public WsEventBase(ChannelHandlerContext ctx) {
        this.ctx=ctx;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

}
