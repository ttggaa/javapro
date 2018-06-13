package com.kariqu.wssrv.app.room;

/**
 * Created by simon on 12/05/2018.
 */
public class CoinsEventDropped {
    private final String deviceId;
    private final int numCoins;

    public CoinsEventDropped(String deviceId, int numCoins) {
        if (deviceId == null)
            deviceId = "";
        this.deviceId=deviceId;
        this.numCoins=numCoins;
    }

    public String getDeviceId() {
        return deviceId!=null?deviceId:"";
    }

    public int getNumCoins() {
        return numCoins;
    }
}
