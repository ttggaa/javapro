package com.kariqu.tyt.http.model;

import com.kariqu.tyt.common.persistence.entity.MissionEntity;
import com.kariqu.tyt.common.persistence.entity.UserMissionEntity;

public class UserMissionModel {
    private int missionId;
    private int currentGoal;
    private int state;      // 0-未完成 1-未领取 2-已领取
    private String title;
    private int goal;
    private int rewardCoin;

    public UserMissionModel(UserMissionEntity entity, MissionEntity missionEntity) {
        this.missionId = entity.getMissionId();
        this.currentGoal = entity.getCurrentGoal();
        this.state = entity.getState();

        this.title = missionEntity.getTitle();
        this.goal = missionEntity.getGoal();
        this.rewardCoin = missionEntity.getRewardCoin();
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public int getCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(int currentGoal) {
        this.currentGoal = currentGoal;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getRewardCoin() {
        return rewardCoin;
    }

    public void setRewardCoin(int rewardCoin) {
        this.rewardCoin = rewardCoin;
    }
}
