package com.kariqu.wssrv.app.tcp;

/**
 * Created by simon on 11/05/2018.
 */
public interface DeviceEventListener {
    void onDropCoins(String deviceId, int coinsNum);
    void onQueryReward(String deviceId, int coinsNum);
    void onDropConisConfirm(String deviceId);
    void onQueryDeviceId(String deviceId);
    void onDeviceOffline(String deviceId);
}
