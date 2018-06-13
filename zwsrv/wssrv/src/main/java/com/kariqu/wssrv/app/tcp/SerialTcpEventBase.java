package com.kariqu.wssrv.app.tcp;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 10/05/2018.
 */
public class SerialTcpEventBase {

    private final ChannelHandlerContext ctx;

    public SerialTcpEventBase(ChannelHandlerContext ctx) {
        this.ctx=ctx;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }
}
