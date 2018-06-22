package com.kariqu.tyt.common.persistence.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tyt_coins_log")
public class CoinsLogEntity {
    public static final int TYPE_MISSION_REWARD = 1;    // 任务奖励
    public static final int TYPE_SIGIN_REWARD = 2;      // 签到奖励
    public static final int TYPE_REGISTER   = 3;        // 注册奖励

    public static final int TYPE_SONG       = 200;      // 点歌

    public static final int TYPE_REBIRTH    = 300;      // 复活

    public CoinsLogEntity() {
        this.id = 0;
        this.userId = 0;
        this.type = 0;
        this.coinsChange = 0;
        this.coinsReamin = 0;
        this.createtime = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "coins_change", nullable = false)
    private int coinsChange;

    @Column(name = "coins_remain", nullable = false)
    private int coinsReamin;

    @Column(name = "createtime", nullable = false)
    private Date createtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCoinsChange() {
        return coinsChange;
    }

    public void setCoinsChange(int coinsChange) {
        this.coinsChange = coinsChange;
    }

    public int getCoinsReamin() {
        return coinsReamin;
    }

    public void setCoinsReamin(int coinsReamin) {
        this.coinsReamin = coinsReamin;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
