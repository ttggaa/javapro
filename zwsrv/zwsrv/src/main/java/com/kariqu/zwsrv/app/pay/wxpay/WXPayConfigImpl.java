package com.kariqu.zwsrv.app.pay.wxpay;

import java.io.InputStream;

/**
 * Created by simon on 18/12/17.
 */
public class WXPayConfigImpl implements WXPayConfig {

    public static final String appId = "wx10772583ba2a35a4";
    public static final String key = "0e3fbc2a4fbb4707a14184e6323b327c";
    public static final String mchID = "1493968322";

    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return mchID;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    //0e3fbc2a4fbb4707a14184e6323b327c
}
