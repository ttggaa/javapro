package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 6/15/16.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_auth")
public class Auth extends VersionedTimedEntity {

    public Auth() {
        this.appId="";
        this.securityHash="";
        this.timeoffset="";
    }

    @Id
    @Column(name="auth_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int authId;

    @Column(name="appid", nullable = false)
    private String appId;

    @Column(name="user_id", nullable = false)
    private int userId;

    //认证类型:email,phone,weibo,username,weixin
    @Column(name="auth_type", nullable = false)
    private String authType;

    @Column(name="identifier", nullable = false)
    private String identifier;

    @Column(name="credential", nullable = false)
    private String credential;

    @Column(name="verified", nullable = false)
    private int verified;

    @Column(name="security_hash", nullable = false)
    private String securityHash;

    @Column(name="status", nullable = false)
    private int status;

    @Column(name="timeoffset", nullable = false)
    private String timeoffset;

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getSecurityHash() {
        return securityHash;
    }

    public void setSecurityHash(String securityHash) {
        this.securityHash = securityHash;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimeoffset() {
        return timeoffset;
    }

    public void setTimeoffset(String timeoffset) {
        this.timeoffset = timeoffset;
    }
}


//CREATE TABLE `zww_auth` (
//        `auth_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `appid` varchar(16) NOT NULL DEFAULT '',
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `auth_type` varchar(16) NOT NULL DEFAULT '' COMMENT '认证类型:email,phone,weibo,username,weixin',
//        `identifier` varchar(64) NOT NULL DEFAULT '认证标识唯一:email,phone,weiboUID,username,微信UserName',
//        `credential` varchar(64) NOT NULL DEFAULT '',
//        `verified` tinyint(1) NOT NULL DEFAULT '0',
//        `security_hash` varchar(64) NOT NULL DEFAULT '',
//        `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '暂时没有用',
//        `timeoffset` varchar(4) NOT NULL DEFAULT '9999' COMMENT '时区',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0' COMMENT '注册时间',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0' COMMENT '注册时间',
//        PRIMARY KEY (`auth_id`),
//        KEY `user_id` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;


//zww_auth {deivce_id,deivce_brand,os,sys_lang,sys_version,sys_model}
//        zww_user { }
//
//        deivce_id    //设备id
//        deivce_brand //手机厂商
//        os      //android/ios/win/mac
//        lang    //获取当前手机系统语言
//        version //系统版本号
//        model   //手机型号
//
//
//
//        http://blog.csdn.net/zhuwentao2150/article/details/51946387
//
//        device_token
//        device_model [MI,]
//        device_os
//        device_os_version
