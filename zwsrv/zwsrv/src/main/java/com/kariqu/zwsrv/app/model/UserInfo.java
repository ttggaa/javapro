package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.thelib.persistance.entity.User;

/**
 * Created by simon on 25/11/17.
 */
public class UserInfo extends UserBaseInfo {

    private int userId;
    private int adminId;
    private String invitationCode;
    private int invitorId;
    private String invitorCode; //邀请者的邀请码
    private int role;
    private String nickName;
    private int gender;
    private String phoneNumber;
    private String front;
    private String avatar;
    private int avatarStatus;
    private int newRegister;
    private int invitationCount;
    private int invitationCoin;

    public UserInfo() {
        invitationCode="";
        invitorCode="";
        nickName="";
        phoneNumber="";
        front="";
        avatar="";
    }

    public UserInfo(User user) {
        this.userId=user.getUserId();
        this.adminId=user.getAdminId();
        this.invitationCode=user.getInvitationCode();
        this.invitorId=user.getInvitorId();
        this.invitorCode=user.getInvitorCode();
        this.invitationCount=user.getInvitationCount();
        this.invitationCoin = user.getInvitationCount() * 30;
        this.role=user.getRole();
        this.nickName=user.getNickName();
        this.gender=user.getGender();
        this.phoneNumber=user.getPhoneNumber();
        this.front=user.getFront();

        this.avatarStatus=user.getAvatarStatus();

        this.avatar= URLHelper.fullUrl(user.getAvatar());
        this.newRegister = 0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getNewRegister() {
        return newRegister;
    }

    public void setNewRegister(int newRegister) {
        this.newRegister = newRegister;
    }

    public int getInvitationCount() {
        return invitationCount;
    }

    public void setInvitationCount(int invitationCount) {
        this.invitationCount = invitationCount;
    }

    public int getInvitationCoin() {
        return invitationCoin;
    }

    public void setInvitationCoin(int invitationCoin) {
        this.invitationCoin = invitationCoin;
    }
}
