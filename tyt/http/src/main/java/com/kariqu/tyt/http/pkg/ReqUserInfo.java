package com.kariqu.tyt.http.pkg;

public class ReqUserInfo {
    private int userId;

    public ReqUserInfo() {
        this.userId = 0;
    }

    public boolean isValid() {
        if (this.userId <= 0)
            return false;
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
