package com.kariqu.zwsrv.app.pay.wxpay;

import java.io.InputStream;

/**
 * Created by simon on 17/12/17.
 */
public class Config implements WXPayConfig {


    @Override
    public String getAppID() {
        return "wx10772583ba2a35a4";
    }

    @Override
    public String getMchID() {
        return "1493968322";
    }

    @Override
    public String getKey() {
        return "0e3fbc2a4fbb4707a14184e6323b327c";
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
