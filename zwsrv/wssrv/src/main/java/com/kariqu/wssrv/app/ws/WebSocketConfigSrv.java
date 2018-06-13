package com.kariqu.wssrv.app.ws;

import org.apache.commons.configuration.CompositeConfiguration;

/**
 * Created by simon on 10/05/2018.
 */
public class WebSocketConfigSrv extends WebSocketConfigApp {

    public WebSocketConfigSrv(CompositeConfiguration config) {
        super(config);

        this.sessionType=WsSessionType.SERVER;
        if (config!=null) {
            this.port=config.getInt("srv.port");
            this.readTimeout=config.getInt("srv.read-timeout");
        }
    }
}
