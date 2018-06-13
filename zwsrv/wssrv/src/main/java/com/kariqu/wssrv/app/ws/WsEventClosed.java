package com.kariqu.wssrv.app.ws;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 10/05/2018.
 */
public class WsEventClosed extends WsEventBase {
    public WsEventClosed(ChannelHandlerContext ctx) {
        super(ctx);
    }
}
