package com.kariqu.wssrv.app.ws;

public class ServerLogin {
    private String userID;
    private String userName;

    public ServerLogin()
    {
        this.userID = "";
        this.userName = "";
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
