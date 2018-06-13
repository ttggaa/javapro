package com.kariqu.wssrv.app.ws;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 10/05/2018.
 */
public class WsEventRoute {

    private final boolean toAllApp;
    private final List<String> connIds;
    private final String content;

    public WsEventRoute(List<String> connIds, String content) {
        this.toAllApp=connIds==null?true:false;
        this.connIds=connIds!=null?connIds:new ArrayList<>();
        this.content=content!=null?content:"";
    }

    public boolean isToAllApp() {
        return toAllApp;
    }

    public List<String> getConnIds() {
        return connIds;
    }

    public String getContent() {
        return content;
    }

}
