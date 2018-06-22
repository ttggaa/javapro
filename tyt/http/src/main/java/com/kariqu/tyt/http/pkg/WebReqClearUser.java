package com.kariqu.tyt.http.pkg;

public class WebReqClearUser {
    private int userId;
    private String key;

    public WebReqClearUser() {
        this.userId = 0;
        this.key = "";
    }

    public boolean isValid() {
        if (userId <= 0)
            return false;
        if (key == null)
            return false;
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
