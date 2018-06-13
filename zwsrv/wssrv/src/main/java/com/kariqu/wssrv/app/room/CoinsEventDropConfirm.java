package com.kariqu.wssrv.app.room;

public class CoinsEventDropConfirm {
    private final String deviceId;

    public CoinsEventDropConfirm(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
