package com.kariqu.wssrv.app.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by simon on 09/05/2018.
 */
public class SerialTcpDevice {
    private static final Logger logger = LoggerFactory.getLogger(SerialTcpDevice.class);

    private ChannelHandlerContext ctx;
    private String deviceId;
    private SerialTcpDeviceManager mgr;

    public SerialTcpDevice(ChannelHandlerContext ctx, SerialTcpDeviceManager mgr) {
        this.ctx=ctx;
        this.deviceId="";
        this.mgr = mgr;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public String getDeviceId() {
        return deviceId!=null?deviceId:"";
    }

    public void handleReceivedPkg(SerialTcpEventReceivedData pkg) {
        DeviceCmd.CmdId id = pkg.cmdId();
        switch (id) {
        case QUERY_DEVICE_ID:
            handleQueryDeviceId(pkg);
            break;
        case DROP_COINS:
            handleDropCoins(pkg);
            break;
        case DROP_COINS_CONFIRM:
            handleDropCoinisConfirm(pkg);
            break;
        case QUERY_REWARD_COINS:
            handleQueryRewardCoins(pkg);
            break;
        default:
            logger.warn("unknwon recevied tbj pkg cmdId: {} devId: {}", id, deviceId);
        }
    }

    private void handleQueryDeviceId(SerialTcpEventReceivedData pkg) {
        this.deviceId = pkg.cmdDataAsString();
        logger.info("update deviceId: {} channelId: {} ", deviceId, ctx.channel().id().asShortText());
        mgr.onQueryDeviceId(this);
    }

    private void handleDropCoins(SerialTcpEventReceivedData pkg) {
        int dropCoinsNum = pkg.cmdDataAsInt();
        //logger.info("devId: {} dropCoins: {}", deviceId, dropCoinsNum);
        mgr.onDropCoins(this.deviceId, dropCoinsNum);
    }

    private void handleQueryRewardCoins(SerialTcpEventReceivedData pkg) {
        int rewardCoinsNum = pkg.cmdDataAsInt();
        //logger.info("devId: {} rewardCoins: {}", deviceId, rewardCoinsNum);
        mgr.onQueryReward(this.deviceId, rewardCoinsNum);
    }

    private void handleDropCoinisConfirm(SerialTcpEventReceivedData pkg) {
        mgr.onDropConisConfirm(this.deviceId);
    }

    public void sendCommand(byte[] cmd) {
        if (cmd!=null&&cmd.length>0) {
            getCtx().writeAndFlush(cmd);
        }
    }

}
