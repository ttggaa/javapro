package com.kariqu.wssrv.app.ws;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 10/05/2018.
 */
public class WsEventCmd extends WsEventBase {

    private final WsCmdHead cmdHead;
    private final String jsonString;

    public WsEventCmd(ChannelHandlerContext ctx, WsCmdHead cmdHead, String jsonString) {
        super(ctx);
        this.cmdHead=cmdHead;
        this.jsonString=jsonString;
    }

    @Override
    public ChannelHandlerContext getCtx() {
        return super.getCtx();
    }

    public WsCmdHead getCmdHead() {
        return cmdHead;
    }

    public String getJsonString() {
        return jsonString;
    }
}
