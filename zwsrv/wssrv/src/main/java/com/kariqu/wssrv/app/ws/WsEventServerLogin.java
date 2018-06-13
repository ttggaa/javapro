package com.kariqu.wssrv.app.ws;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 11/05/2018.
 */
public class WsEventServerLogin extends WsEventBase {

    private final ServerLogin serverLogin;

    public WsEventServerLogin(ChannelHandlerContext ctx, ServerLogin serverLogin) {
        super(ctx);
        this.serverLogin=serverLogin;
    }

}
