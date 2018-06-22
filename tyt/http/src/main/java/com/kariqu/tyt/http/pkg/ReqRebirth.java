package com.kariqu.tyt.http.pkg;

public class ReqRebirth {
    private int userId;

    public ReqRebirth() {
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
