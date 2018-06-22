package com.kariqu.tyt.http.pkg;

public class WebReqChangeCoin {
    private int userId;
    private int coins;
    private String key;

    public WebReqChangeCoin() {
        this.userId = 0;
        this.coins = 0;
        this.key = "";
    }

    public boolean isValid() {
        if (userId <= 0)
            return false;
        if (key == null || key.isEmpty())
            return false;
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public int getCoins() {
        return coins;
    }

    public String getKey() {
        return key;
    }
}
