package com.kariqu.tyt.http.pkg;

public class ReqSong {
    private int userId;

    public ReqSong() {
        this.userId = 0;
    }

    public boolean isValid() {
        if (userId <= 0)
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
