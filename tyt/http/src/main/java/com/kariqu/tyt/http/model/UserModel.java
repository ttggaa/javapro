package com.kariqu.tyt.http.model;

import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.http.utility.LocalDateTimeUtils;

import java.time.LocalDateTime;

public class UserModel {
    private int userId;
    private int coins;
    private int siginId;
    private int canSignin;
    private int newUser;        // 1-新用户

    public UserModel() {
        this.userId = 0;
        this.coins = 0;
        this.siginId = 0;
        this.canSignin = 0;
        this.newUser = 0;
    }

    public UserModel(UserEntity entity, boolean isNewUser) {
        this.userId = entity.getUserId();
        this.coins = entity.getCoins();
        this.siginId = entity.getSigninId();
        this.newUser = isNewUser ? 1 : 0;
        LocalDateTime tnow = LocalDateTime.now();
        boolean isSameDay = LocalDateTimeUtils.isSameDay(tnow, LocalDateTimeUtils.dateToLocalDateTime(entity.getSigninTm()));
        this.canSignin = isSameDay ? 0 : 1;
    }

    public UserModel(UserEntity entity) {
        this.userId = entity.getUserId();
        this.coins = entity.getCoins();
        this.siginId = entity.getSigninId();

        LocalDateTime tnow = LocalDateTime.now();
        boolean isSameDay = LocalDateTimeUtils.isSameDay(tnow, LocalDateTimeUtils.dateToLocalDateTime(entity.getSigninTm()));
        this.canSignin = isSameDay ? 0 : 1;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSiginId() {
        return siginId;
    }

    public void setSiginId(int siginId) {
        this.siginId = siginId;
    }

    public int getCanSignin() {
        return canSignin;
    }

    public void setCanSignin(int canSignin) {
        this.canSignin = canSignin;
    }

    public int getNewUser() {
        return newUser;
    }

    public void setNewUser(int newUser) {
        this.newUser = newUser;
    }
}
