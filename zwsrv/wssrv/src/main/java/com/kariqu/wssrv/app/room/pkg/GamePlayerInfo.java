package com.kariqu.wssrv.app.room.pkg;

/**
 * Created by simon on 10/05/2018.
 */
public class GamePlayerInfo implements PackageChecker {
    private String userID;
    private String userName;

    @Override
    public boolean isValid() {
        if (userID == null || userID.isEmpty()
                || userName == null || userName.isEmpty())
            return false;
        return true;
    }

    public GamePlayerInfo(String userID, String userName) {
        this.userID = userID;
        this.userName=userName;
    }

    public GamePlayerInfo() {
        userID ="";
        userName="";
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
