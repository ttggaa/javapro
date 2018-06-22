package com.kariqu.tyt.http.pkg;

public class ReqMissionReward {
    private int userId;
    private int missionId;

    public ReqMissionReward() {
        this.userId = 0;
        this.missionId = 0;
    }

    public boolean isValid() {
        if (userId <= 0)
            return false;
        if (missionId <= 0)
            return false;
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public int getMissionId() {
        return missionId;
    }
}
