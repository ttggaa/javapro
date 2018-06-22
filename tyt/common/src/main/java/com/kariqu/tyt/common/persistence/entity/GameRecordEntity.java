package com.kariqu.tyt.common.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "tyt_game_record")
public class GameRecordEntity extends DatetimeEntity {

    public GameRecordEntity() {
        this.id = 0;
        this.userId = 0;
        this.duration = 0;
        this.totalScore = 0;
        this.totalCnt = 0;
        this.successCnt = 0;
        this.rebirthCnt = 0;
        this.perfectCnt = 0;
        this.heartCnt = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "user_id", updatable = false, nullable = false)
    private int userId;

    @Column(name = "duration", updatable = false, nullable = false)
    private int duration;

    @Column(name = "total_score", updatable = false, nullable = false)
    private int totalScore;

    @Column(name = "total_cnt", updatable = false, nullable = false)
    private int totalCnt;

    @Column(name = "success_cnt", updatable = false, nullable = false)
    private int successCnt;

    @Column(name = "rebirth_cnt", updatable = false, nullable = false)
    private int rebirthCnt;

    @Column(name = "perfect_cnt", updatable = false, nullable = false)
    private int perfectCnt;

    @Column(name = "heart_cnt", updatable = false, nullable = false)
    private int heartCnt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
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
