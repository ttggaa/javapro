package com.kariqu.wssrv.app.tcp;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by simon on 10/05/2018.
 */
public class SerialTcpEventQueryDeviceId extends SerialTcpEventBase {

    public SerialTcpEventQueryDeviceId(ChannelHandlerContext ctx) {
        super(ctx);
    }
}
