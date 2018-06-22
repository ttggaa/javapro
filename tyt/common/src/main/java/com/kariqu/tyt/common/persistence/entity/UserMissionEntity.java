package com.kariqu.tyt.common.persistence.entity;


import javax.persistence.*;

@Entity
@Table(name = "tyt_user_mission")
public class UserMissionEntity extends DatetimeEntity {

    public static final int STATE_UNFINISH = 0; // 未完成
    public static final int STATE_UNREWARD = 1; // 未领取
    public static final int STATE_REWARDED = 2; // 已领取

    public static final int USER_MISSION_MAX = 3; // 玩家任务最多个数

    public UserMissionEntity() {
        this.userId = 0;
        this.index = 0;
        this.missionId = 0;
        this.currentGoal = 0;
        this.state = STATE_UNFINISH;
    }

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "index", nullable = false)
    private int index;

    @Column(name = "mission_id", nullable = false)
    private int missionId;

    @Column(name = "current_goal", nullable = false)
    private int currentGoal;

    @Column(name = "state", nullable = false)
    private int state;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
}
