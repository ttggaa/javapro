package com.kariqu.wssrv.app.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TbjDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(TbjDecoder.class);

    private static String arrToString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i != arr.length; ++i) {
            String hex = String.format("%02X", arr[i] & 0xFF);
            sb.append(hex);
            sb.append(' ');
        }
        return sb.toString();
    }

    private static SerialTcpEventReceivedData parse(ChannelHandlerContext ctx, ByteBuf msg) {
        if (msg.readableBytes() < 2)
            return null;
        // 开始码
        int code1 = msg.getByte(msg.readerIndex() + 0) & 0xFF;
        if (code1 != 0x8A) {
            throw new RuntimeException("unknown code1: " + code1 + " " + Thread.currentThread().getName());
        }

        // 长度
        int code2 = msg.getByte(msg.readerIndex() + 1) & 0xFF;
        if (code2 > 0x0A + 3) {
            throw new RuntimeException("length too long code1: " + code1 + " code2:" + code2 + " " + Thread.currentThread().getName());
        }

        // 缓冲区长度不够
        if (msg.readableBytes() < (code2 + 3))
            return null;
        byte[] arr = new byte[code2 + 3];
        msg.readBytes(arr);
        SerialTcpEventReceivedData data = new SerialTcpEventReceivedData(ctx, arr);
        if (!data.isValid()) {
            throw new RuntimeException("SerialTcpEventReceivedData invalid: " + data.toString() + " #  " + Thread.currentThread().getName()
                    + "    code1:" +code1 + " code2:" + code2);
        } else {
            //logger.info("received Tbj cmd: {} {}", data.cmdId(), data);
        }
        return data;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        //logger.info("---> readableBytes {}", msg.readableBytes());
        while (true) {
            SerialTcpEventReceivedData data = parse(ctx, msg);
            if (data == null)
                break;
            else
                out.add(data);
        }
    }
}
