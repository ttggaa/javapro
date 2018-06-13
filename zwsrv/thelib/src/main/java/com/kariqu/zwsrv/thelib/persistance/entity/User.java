package com.kariqu.zwsrv.thelib.persistance.entity;


import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import com.kariqu.zwsrv.thelib.security.SecurityConstants;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 4/26/16.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_users")
public class User extends VersionedTimedEntity {

    public static final int kAvatarStatusNormal  =0;//正常
    public static final int kAvatarStatusInReview=1;//审核中

    public static final int Gender_Unknown = 0;
    public static final int Gender_Male = 1;
    public static final int Gender_Female = 2;

    public User(){
        this.unionId="";

        this.invitationCode="";
        this.invitorCode="";
        this.role = SecurityConstants.ROLE_TYPE_USER;
        this.gender = Gender_Unknown;
        this.nickName = "";
        this.phoneNumber = "";
        this.avatar = "";
        this.front = "";

        this.address = "";
        this.sig="";

        this.qq="";
        this.wechat="";

        this.intro="";

        this.geoHash="";

        this.lastIp="";
        this.registerReward = 0;
        this.appChannel = "";
        this.regAppChannel = "";
    }

    @Id
    @Column(name="user_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int userId;

    @Column(name="unionid", nullable = false)
    private String unionId;

    @Column(name="admin_id", nullable = false)
    private int adminId;

    @Column(name="invitation_code", nullable = false)
    private String invitationCode;

    @Column(name="invitor_id", nullable = false)
    private int invitorId;

    @Column(name="invitor_code", nullable = false)
    private String invitorCode;

    @Column(name="invitation_count", nullable = false)
    private int invitationCount;

    @Column(name="role")
    private int role;

    //默认为user表的username
    @Column(name="nickname", nullable = false)
    private String nickName;

    @Column(name="gender", nullable = false)
    private int gender;

    //'电话号码,如果用户使用手机注册则为user_auth表中的号码'
    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    @Column(name="front", nullable = false)
    private String front;

    @Column(name="avatar", nullable = false)
    private String avatar;

    @Column(name="avatar_status", nullable = false)
    private int avatarStatus;

    @Column(name="birth_year", nullable = false)
    private int birthYear;

    @Column(name="birth_month", nullable = false)
    private int birthMonth;

    @Column(name="birth_day", nullable = false)
    private int birthDay;

    private int province;

    private int city;

    private int dist;

    private String address;

    private String sig;

    private String intro;

    private String qq;

    private String wechat;

    @Column(name="last_login", nullable = false)
    private long lastLogin;

    @Column(name="last_ip", nullable = false)
    private String lastIp;

    @Column(name="lat")
    private double lat;

    @Column(name="lng")
    private double lng;

    @Column(name="geo_hash")
    private String geoHash;

    @Column(name = "register_reward")
    private int registerReward;

    @Column(name = "app_channel")
    private String appChannel;

    @Column(name = "reg_app_channel")
    private String regAppChannel;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public int getInvitorId() {
        return invitorId;
    }

    public void setInvitorId(int invitorId) {
        this.invitorId = invitorId;
    }

    public String getInvitorCode() {
        return invitorCode;
    }

    public void setInvitorCode(String invitorCode) {
        this.invitorCode = invitorCode;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAvatarStatus() {
        return avatarStatus;
    }

    public void setAvatarStatus(int avatarStatus) {
        this.avatarStatus = avatarStatus;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public int getInvitationCount() {
        return invitationCount;
    }

    public void setInvitationCount(int invitationCount) {
        this.invitationCount = invitationCount;
    }

    public int getRegisterReward() {
        return registerReward;
    }

    public void setRegisterReward(int registerReward) {
        this.registerReward = registerReward;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public String getRegAppChannel() {
        return regAppChannel;
    }

    public void setRegAppChannel(String regAppChannel) {
        this.regAppChannel = regAppChannel;
    }
}

//
//CREATE TABLE `zww_users` (
//        `user_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `unionid` varchar(64) NOT NULL DEFAULT '' COMMENT '如使用微信登录，为微信平台用户的unionid',
//        `admin_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `invitation_code` varchar(32) NOT NULL DEFAULT '' COMMENT '',
//        `invitor_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `role` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户类型:admin,user,author',
//        `nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '默认为user表的username',
//        `gender` tinyint(1) NOT NULL DEFAULT '0',
//        `phone_number` varchar(32) NOT NULL DEFAULT '' COMMENT '电话号码,如果用户使用手机注册则为user_auth表中的号码',
//        `front` varchar(255) NOT NULL DEFAULT '',
//        `avatar` varchar(255) NOT NULL DEFAULT '',
//        `avatar_status` tinyint(1) NOT NULL DEFAULT '0',
//        `birth_year` smallint(6) UNSIGNED NOT NULL DEFAULT '0',
//        `birth_month` tinyint(3) UNSIGNED NOT NULL DEFAULT '0',
//        `birth_day` tinyint(3) UNSIGNED NOT NULL DEFAULT '0',
//        `province` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `city` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `dist` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `address` varchar(255) NOT NULL DEFAULT '' COMMENT '地址',
//        `sig` varchar(255) NOT NULL DEFAULT '' COMMENT '个性签名',
//        `intro` text NOT NULL COMMENT '自我介绍',
//        `qq` varchar(32) NOT NULL DEFAULT '',
//        `wechat` varchar(32) NOT NULL DEFAULT '',
//        `last_login` bigint(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '最后登录时间',
//        `last_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '最后登录IP',
//        `lng` decimal(12,8) NOT NULL DEFAULT '0.00000000' COMMENT '经度',
//        `lat` decimal(12,8) NOT NULL DEFAULT '0.00000000' COMMENT '纬度',
//        `geo_hash` varchar(32) NOT NULL DEFAULT '' COMMENT 'geohash编码',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`user_id`),
//        KEY `createtime` (`createtime`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=12;
