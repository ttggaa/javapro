package com.kariqu.wssrv.app.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by simon on 08/05/2018.
 */
public class SerialTcpServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SerialTcpServer.class);

    private final SerialTcpServerConfig tcpServerConfig;

    private boolean isRunning;

    public SerialTcpServer(SerialTcpServerConfig tcpServerConfig) {
        this.tcpServerConfig=tcpServerConfig;
        this.isRunning = true;
    }

    public void shutdown() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(tcpServerConfig.getBossCount());
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        logger.info("----> tcp thread {}", Thread.currentThread().getName());

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, tcpServerConfig.getBacklog())
                    .option(ChannelOption.SO_KEEPALIVE, tcpServerConfig.isKeepAlive())
//                    .option(ChannelOption.SO_SNDBUF, 32 * 1024) // 设置发送缓冲大小
//                    .option(ChannelOption.SO_RCVBUF, 32 * 1024) // 这是接收缓冲大小
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
//                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                            pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new TbjDecoder());
                            pipeline.addLast(new ByteArrayEncoder());
                            pipeline.addLast(new IdleStateHandler(5, 0, 0),// 心跳控制
                                    new SerialTcpServerHandler());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(tcpServerConfig.getPort()).sync();

            while (isRunning) {
                Thread.sleep(1000);
            }

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();

            logger.info("tcp server shutdown thread: {}", Thread.currentThread().getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            //退出时释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
