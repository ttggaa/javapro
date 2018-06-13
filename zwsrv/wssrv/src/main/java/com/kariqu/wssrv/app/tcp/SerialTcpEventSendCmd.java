package com.kariqu.wssrv.app.tcp;

/**
 * Created by simon on 10/05/2018.
 */
public class SerialTcpEventSendCmd {

    private final String deviceId;
    private final byte[] cmd;

    public SerialTcpEventSendCmd(String deviceId, byte[] cmd) {
        this.deviceId=deviceId!=null?deviceId:"";
        this.cmd=cmd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public byte[] getCmd() {
        return cmd;
    }
}
