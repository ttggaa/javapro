package com.kariqu.tyt.http.pkg;

public class ReqSaveScore {
    private int score;
    private int duration;
    private int totalCnt;
    private int successCnt;
    private int userId;
    private int rebirthCnt;
    private int perfectCnt;
    private int heartCnt;

    public ReqSaveScore() {
        this.score = 0;
        this.duration = 0;
        this.totalCnt = 0;
        this.successCnt = 0;
        this.userId = 0;
        this.rebirthCnt = 0;
        this.perfectCnt = 0;
        this.heartCnt = 0;
    }

    public boolean isValid() {
        if (score < 0)
            return false;
        if (userId <= 0)
            return false;
        return true;
    }

    public int getScore() {
        return score;
    }

    public int getDuration() {
        return duration;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getSuccessCnt() {
        return successCnt;
    }

    public void setSuccessCnt(int successCnt) {
        this.successCnt = successCnt;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public int getRebirthCnt() {
        return rebirthCnt;
    }

    public void setRebirthCnt(int rebirthCnt) {
        this.rebirthCnt = rebirthCnt;
    }

    public int getPerfectCnt() {
        return perfectCnt;
    }

    public void setPerfectCnt(int perfectCnt) {
        this.perfectCnt = perfectCnt;
    }

    public int getHeartCnt() {
        return heartCnt;
    }

    public void setHeartCnt(int heartCnt) {
        this.heartCnt = heartCnt;
    }
}
