package com.kariqu.tyt.http.pkg;

public class ReqPush {
    private int userId;
    private int score;
    private int npcId;
    private int perfect;
    private int barId;

    public ReqPush() {
        this.userId = 0;
        this.score = 0;
        this.npcId = 0;
        this.perfect = 0;
        this.barId = 0;
    }

    public boolean isValid() {
        if (userId <= 0)
            return false;
        if (score < 0)
            return false;
        if (npcId < 0)
            return false;
        if (perfect < 0)
            return false;
        if (barId < 0)
            return false;
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getPerfect() {
        return perfect;
    }

    public int getBarId() {
        return barId;
    }
}
