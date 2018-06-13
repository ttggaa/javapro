package com.kariqu.wssrv.app.ws;

import org.apache.commons.configuration.CompositeConfiguration;

/**
 * Created by simon on 09/05/2018.
 */
public class WebSocketConfigApp {

    public WebSocketConfigApp(CompositeConfiguration config) {
        this.sessionType=WsSessionType.APP;
        if (config!=null) {
            this.port=config.getInt("app.port");
            this.readTimeout=config.getInt("app.read-timeout");
        }
    }
    int port;
    int readTimeout;
    WsSessionType sessionType;


    public int getPort() {
        return port;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public WsSessionType getSessionType() {
        return sessionType;
    }
}
