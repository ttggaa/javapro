package com.kariqu.tyt.http.pkg;

public class ReqGetSignin {
    private int userId;
    public ReqGetSignin() {
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
}
