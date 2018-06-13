package com.kariqu.wssrv.app.ws;

public enum WsCmdType {
    // srv -> app 全部广播
    REQ_BROADCAST_ALL_APP(1, "req_broadcast_all_app"),

    // 路由消息srv<------>app   srv<------>srv    app<------>app
    REQ_ROUTER(2, "req_router"),

    REQ_LOGIN(7, "req_login"),


    // rsp_error
    RSP_REPEATED_LOGIN(10000 + 0, "rsp_repeated_login"),
    RSP_LOGIN(10007, "rsp_login"),
    RSP_BROADCAST_ALL_APP(10010, "rsp_broadcast_all_app"),

    REQ_TBJ_CMD(5000, "tbj_cmd"),
    REQ_TBJ_JOIN_ROOM(5001, "tbj_join_room"),
    REQ_TBJ_LEAVE_ROOM(5002, "tbj_leave_room"),
    RSP_TBJ_JOIN_ROOM(5003, "rsp_tbj_join_room");


    private int type;
    private String name;

    public int getType()
    {
        return type;
    }

    public String getName() {
        return name;
    }

    public static WsCmdType valueOf(int val)
    {
        switch (val)
        {
            case 1: return REQ_BROADCAST_ALL_APP;
            case 2: return REQ_ROUTER;
            case 7: return REQ_LOGIN;

            case 5000 : return REQ_TBJ_CMD;
            case 5001 : return REQ_TBJ_JOIN_ROOM;
            case 5002 : return REQ_TBJ_LEAVE_ROOM;
            case 5003 : return RSP_TBJ_JOIN_ROOM;

            case 10000 + 0 : return RSP_REPEATED_LOGIN;
            case 10000 + 7 : return RSP_LOGIN;

            default:
                return null;
        }
    }

    private WsCmdType(int type, String name)
    {
        this.type = type;
        this.name = name;
    }

    private WsCmdType(int type)
    {
        this.type = type;
        this.name = null;
    }
}