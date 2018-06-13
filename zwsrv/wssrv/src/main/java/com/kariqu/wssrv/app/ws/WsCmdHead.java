package com.kariqu.wssrv.app.ws;

import java.util.ArrayList;
import java.util.List;

public class WsCmdHead {
    private String          connid;
    private List<String>    to;
    private int             type;
    private String          content;

    public WsCmdHead()
    {
        this.connid = "";
        this.to = new ArrayList<>();
        this.type = 0;
        this.content = "";
    }

    public String getConnid() {
        return connid;
    }

    public void setConnid(String connid) {
        this.connid = connid;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
