package com.kariqu.tyt.http.pkg;

public class ReqGetMission {
    private int userId;

    public ReqGetMission() {
        this.userId = 0;
    }

    public boolean isValid() {
        if (userId == 0)
            return false;
        return true;
    }

    public int getUserId() {
        return userId;
    }
}
