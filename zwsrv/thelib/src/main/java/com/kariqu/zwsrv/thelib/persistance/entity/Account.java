package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by simon on 14/02/17.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_account")
public class Account extends VersionedTimedEntity {

    public Account() {
        this.userId = 0;
        this.coins = 0;
        this.availableCoins = 0;
        this.money = 0;
        this.availableMoney = 0;
        this.points = 0;
        this.availablePoints = 0;
        this.newUserGift = 0;
        this.giftDatetime = new Date(0);
        this.chargeFirstTime = 0;
        this.chargeFirstTimeDatetime = new Date(0);
    }

    public Account(Account account) {
        this.userId=account.getUserId();
        this.coins=account.getCoins();
        this.availableCoins=account.getAvailableCoins();
        this.money=account.getMoney();
        this.availableMoney=account.getAvailableMoney();
        this.points=account.getPoints();
        this.availablePoints=account.getAvailablePoints();
        this.newUserGift = account.getNewUserGift();
        this.giftDatetime = new Date(account.getGiftDatetime().getTime());
        this.chargeFirstTime = account.getChargeFirstTime();
        this.chargeFirstTimeDatetime = new Date(account.getChargeFirstTimeDatetime().getTime());
    }

    @Id
    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="coins", nullable = false)
    private int coins;

    @Column(name="available_coins", nullable = false)
    private int availableCoins;

    @Column(name="money", nullable = false)
    private int money;

    @Column(name="available_money", nullable = false)
    private int availableMoney;

    @Column(name="points", nullable = false)
    private int points;

    @Column(name="available_points", nullable = false)
    private int availablePoints;

    @Column(name="new_user_gift", nullable = false)
    private int newUserGift;

    @Column(name="gift_datetime", nullable = false)
    private Date giftDatetime;

    @Column(name="charge_first_time", nullable = false)
    private int chargeFirstTime;

    @Column(name="charge_ft_datetime", nullable = false)
    private Date chargeFirstTimeDatetime;


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

    public int getAvailableCoins() {
        return availableCoins;
    }

    public void setAvailableCoins(int availableCoins) {
        this.availableCoins = availableCoins;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(int availableMoney) {
        this.availableMoney = availableMoney;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public int getNewUserGift() {
        return newUserGift;
    }

    public void setNewUserGift(int newUserGift) {
        this.newUserGift = newUserGift;
    }

    public Date getGiftDatetime() {
        return giftDatetime;
    }

    public void setGiftDatetime(Date giftDatetime) {
        this.giftDatetime = giftDatetime;
    }

    public int getChargeFirstTime() {
        return chargeFirstTime;
    }

    public void setChargeFirstTime(int chargeFirstTime) {
        this.chargeFirstTime = chargeFirstTime;
    }

    public Date getChargeFirstTimeDatetime() {
        return chargeFirstTimeDatetime;
    }

    public void setChargeFirstTimeDatetime(Date chargeFirstTimeDatetime) {
        this.chargeFirstTimeDatetime = chargeFirstTimeDatetime;
    }
}

//
//CREATE TABLE `zww_account` (
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏的金币',
//        `available_coins` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `money` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏',
//        `available_money` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `points` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '积分',
//        `available_points` int(10) NOT NULL DEFAULT '0',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;