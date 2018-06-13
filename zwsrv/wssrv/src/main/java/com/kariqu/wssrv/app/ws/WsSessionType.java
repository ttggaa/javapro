package com.kariqu.wssrv.app.ws;

/**
 * Created by simon on 10/05/2018.
 */
public enum WsSessionType {
    UNKNOWN(-1),
    APP(0),
    SERVER(1);

    private int type;

    private WsSessionType(int type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        switch (type) {
            case 0 : return "APP";
            case 1 : return  "SERVER";
            default:
                return "UNKNOWN";
        }
    }
}
