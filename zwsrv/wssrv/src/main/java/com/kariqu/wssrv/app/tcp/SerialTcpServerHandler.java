package com.kariqu.wssrv.app.tcp;

import io.netty.channel.*;

/**
 * Created by simon on 09/05/2018.
 */
public class SerialTcpServerHandler extends SimpleChannelInboundHandler<SerialTcpEventReceivedData> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SerialTcpDeviceManager.getInstance().put(new SerialTcpEventAccept(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SerialTcpDeviceManager.getInstance().put(new SerialTcpEventClosed(ctx));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SerialTcpEventReceivedData msg) throws Exception {
        SerialTcpDeviceManager.getInstance().put(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //cause.printStackTrace();
        cause.toString();
        closeChannel(ctx);
    }

    void closeChannel(final ChannelHandlerContext ctx) {
        final ChannelId channelId = ctx.channel().id();
        ChannelFuture future = ctx.close();
        future.addListener(
                new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) {
                        SerialTcpDeviceManager.getInstance().put(new SerialTcpEventClosed(ctx));
                    }
                }
        );
    }
}

