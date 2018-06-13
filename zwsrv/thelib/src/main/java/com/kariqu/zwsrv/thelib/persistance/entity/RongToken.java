package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by simon on 08/12/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_rong_token")
public class RongToken extends VersionedTimedEntity {

    public RongToken() {
        userSN="";
        nickName="";
        avatarUrl="";
        token="";
    }

    @Id
    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="user_sn", nullable = false)
    private String userSN;

    @Column(name="nickname", nullable = false)
    private String nickName;

    @Column(name="avatar_url", nullable = false)
    private String avatarUrl;

    @Column(name="token", nullable = false)
    private String token;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserSN() {
        return userSN;
    }

    public void setUserSN(String userSN) {
        this.userSN = userSN;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

//
//CREATE TABLE `zww_rong_token` (
//        `user_id` int(10) NOT NULL DEFAULT '0',
//        `user_sn` varchar(128) NOT NULL DEFAULT '',
//        `nickname` varchar(80) NOT NULL DEFAULT '',
//        `avatar_url` varchar(255) NOT NULL DEFAULT '',
//        `token` varchar(255) NOT NULL DEFAULT '',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


