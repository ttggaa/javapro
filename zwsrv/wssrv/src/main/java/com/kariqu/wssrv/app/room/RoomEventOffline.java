package com.kariqu.wssrv.app.room;

public class RoomEventOffline {
    private String deviceId;

    public RoomEventOffline(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
