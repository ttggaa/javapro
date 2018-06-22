package com.kariqu.tyt.common.persistence.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tyt_user")
public class UserEntity extends DatetimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "platform", updatable = false, nullable = false)
    private String platform;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "wechat_openid", updatable = false, nullable = false)
    private String wechatOpenid;

    @Column(name = "wechat_unionid", updatable = false, nullable = false)
    private String wechatUnionid;

    @Column(name = "coins", nullable = false)
    private int coins;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "mission_refresh_tm", nullable = false)
    private Date missionRefreshTm;

    @Column(name = "signin_id", nullable = false)
    private int signinId;

    @Column(name = "signin_tm", nullable = false)
    private Date signinTm;

    public UserEntity() {
        this.userId = 0;
        this.platform = "";
        this.nickname = "";
        this.wechatOpenid = "";
        this.wechatUnionid = "";
        this.coins = 0;
        this.score = 0;
        this.missionRefreshTm = new Date();
        this.signinId = 0;
        this.signinTm = new Date(0);
    }

    public int getUserId() {
        return userId;
    }

    public String getPlatform() {
        return platform;
    }

    public String getNickname() {
        return nickname;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public String getWechatUnionid() {
        return wechatUnionid;
    }

    public int getCoins() {
        return coins;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid;
    }

    /*
    public void setCoins(int coins) {
        this.coins = coins;
        if (this.coins < 0)
            this.coins = 0;
    }
    */

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getMissionRefreshTm() {
        return missionRefreshTm;
    }

    public void setMissionRefreshTm(Date missionRefreshTm) {
        this.missionRefreshTm = missionRefreshTm;
    }

    public int getSigninId() {
        return signinId;
    }

    public void setSigninId(int signinId) {
        this.signinId = signinId;
    }

    public Date getSigninTm() {
        return signinTm;
    }

    public void setSigninTm(Date signinTm) {
        this.signinTm = signinTm;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeCoins(int coins) {
        this.coins += coins;
        if (this.coins < 0)
            this.coins = 0;
    }

    public void setRegisterCoins(int coins) {
        this.coins = coins;
        if (this.coins < 0)
            this.coins = 0;
    }
}
