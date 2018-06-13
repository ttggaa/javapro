package com.kariqu.wssrv.app.tcp;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 10/05/2018.
 */
public class SerialTcpEventAccept extends SerialTcpEventBase {

    public SerialTcpEventAccept(ChannelHandlerContext ctx) {
        super(ctx);
    }

}
