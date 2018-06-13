package com.kariqu.wssrv.app.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by simon on 10/05/2018.
 */
public class SerialTcpEventReceivedData extends SerialTcpEventBase {
    private static final Logger logger = LoggerFactory.getLogger(SerialTcpEventReceivedData.class);

    private final byte[] buffer;

    public SerialTcpEventReceivedData(ChannelHandlerContext ctx, byte[] bytes) {
        super(ctx);
        buffer = bytes;
    }

    public boolean isValid() {
        if (cmdType() != 0x02) {
            logger.warn(" cmdType 1");
            return false;
        }
        DeviceCmd.CmdId id = cmdId();
        if (id == null) {
            logger.warn(" cmdType 2");
            return false;
        }
        if (id == DeviceCmd.CmdId.QUERY_DEVICE_ID && dataLength() != 0x0A) {
            logger.warn(" cmdType 3");
            return false;
        }
        if (id == DeviceCmd.CmdId.DROP_COINS && dataLength() != 0x04) {
            logger.warn(" cmdType 4");
            return false;
        }
        if (id == DeviceCmd.CmdId.DROP_COINS_CONFIRM && dataLength() != 0x03) {
            logger.warn(" cmdType 5");
            return false;
        }
        if (id == DeviceCmd.CmdId.QUERY_REWARD_COINS && dataLength() != 0x04) {
            logger.warn(" cmdType 6");
            return false;
        }
        return true;
    }

    public int bufferLength() {
        return buffer.length;
    }

    public int dataLength() {
        return buffer[1];
    }

    public int cmdIdAsInt() {
        return buffer[2];
    }
    public DeviceCmd.CmdId cmdId() {
        return DeviceCmd.CmdId.valueOf(cmdIdAsInt());
    }

    public int cmdType() {
        return  buffer[3];
    }

    public String cmdDataAsString() {
        /*
        获取下位机ID（指令ID：01）
        下位机应答：
        开始码	数据长度	指令ID	指令类型	指令数据	校验位	结束码
        8A	0A	01	02	7位ID
        */
        if (cmdId() == DeviceCmd.CmdId.QUERY_DEVICE_ID) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i != 7; ++i) {
                int pos = 4 +i;
                String hex = String.format("%02X", buffer[pos] & 0xFF);
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }
        return "";
    }

    public int cmdDataAsInt() {
        //投币命令（指令ID：02）
        DeviceCmd.CmdId id = cmdId();
        if (id == DeviceCmd.CmdId.DROP_COINS)
            return buffer[4];
        //上位机主动查询计币信号（指令ID：06）
        if (id == DeviceCmd.CmdId.QUERY_REWARD_COINS)
            return buffer[4];
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i != buffer.length; ++i) {
            String hex = String.format("%02X ", buffer[i] & 0xFF);
            sb.append(hex);
        }
        return sb.toString();
    }
}
