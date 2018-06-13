package com.kariqu.wssrv.app.ws;

import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

/**
 * Created by simon on 10/05/2018.
 */
public class CloseFramer extends CloseWebSocketFrame {
    private CloseFramer(StatusCode code)
    {
        super(code.getStatusCode(), code.getStr());
    }

    public static CloseFramer normalClose()
    {
        return new CloseFramer(StatusCode.NORMAL_CLOSE);
    }

    public static CloseFramer repeatedLoginClose()
    {
        return new CloseFramer(StatusCode.REPEATED_LOGIN_CLOSE);
    }

}
