package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimestampEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 14/02/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_account_log")
public class AccountLog extends TimestampEntity {

    public AccountLog() {
        data="";
        extraData = "";
    }

    @Id
    @Column(name="log_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int logId;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="coins", nullable = false)
    private int coins;

    @Column(name="available_coins", nullable = false)
    private int availableCoins;

    @Column(name="coins_changed_type", nullable = false)
    private int coinsChangedType;

    @Column(name="money", nullable = false)
    private int money;

    @Column(name="available_money", nullable = false)
    private int availableMoney;

    @Column(name="money_changed_type", nullable = false)
    private int moneyChangedType;

    @Column(name="points", nullable = false)
    private int points;

    @Column(name="available_points", nullable = false)
    private int availablePoints;

    @Column(name="points_changed_type", nullable = false)
    private int pointsChangedType;

    @Column(name="fee", nullable = false)
    private int fee;

    @Column(name="data", nullable = false)
    private String data;

    @Column(name="extra_data", nullable = false)
    private String extraData;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
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

    public int getAvailableCoins() {
        return availableCoins;
    }

    public void setAvailableCoins(int availableCoins) {
        this.availableCoins = availableCoins;
    }

    public int getCoinsChangedType() {
        return coinsChangedType;
    }

    public void setCoinsChangedType(int coinsChangedType) {
        this.coinsChangedType = coinsChangedType;
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

    public int getMoneyChangedType() {
        return moneyChangedType;
    }

    public void setMoneyChangedType(int moneyChangedType) {
        this.moneyChangedType = moneyChangedType;
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

    public int getPointsChangedType() {
        return pointsChangedType;
    }

    public void setPointsChangedType(int pointsChangedType) {
        this.pointsChangedType = pointsChangedType;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}


//CREATE TABLE `zww_account_log` (
//        `log_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `coins` int(10) NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏的金币',
//        `available_coins` int(10) NOT NULL DEFAULT '0' COMMENT '有效金币',
//        `coins_changed_type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `money` int(10) NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏',
//        `available_money` int(10) NOT NULL DEFAULT '0',
//        `money_changed_type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `points` int(10) NOT NULL DEFAULT '0' COMMENT '积分',
//        `available_points` int(10) NOT NULL DEFAULT '0',
//        `points_changed_type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `fee` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '精确到分,账户变动涉及到的用户花费的钱',
//        `data` text NOT NULL,
//        `timestamp` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`log_id`),
//        KEY `user_id` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;


//CREATE TABLE `zww_account_log` (
//        `log_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `reward_coins` int(10) NOT NULL DEFAULT '0' COMMENT '系统奖励金币',
//        `available_reward_coins` int(10) NOT NULL DEFAULT '0' COMMENT '系统奖励金币',
//        `coins` int(10) NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏的金币',
//        `available_coins` int(10) NOT NULL DEFAULT '0',
//        `reward_money` int(10) NOT NULL DEFAULT '0' COMMENT '系统奖励',
//        `available_reward_money` int(10) NOT NULL DEFAULT '0' COMMENT '系统奖励',
//        `money` int(10) NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏',
//        `available_money` int(10) NOT NULL DEFAULT '0',
//        `points` int(10) NOT NULL DEFAULT '0' COMMENT '积分',
//        `available_points` int(10) NOT NULL DEFAULT '0',
//        `changed_by` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `description` varchar(80) NOT NULL DEFAULT '',
//        `timestamp` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`log_id`),
//        KEY `user_id` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;


//CREATE TABLE `zww_account_log` (
//        `log_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `coins` int(10) NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏的金币',
//        `available_coins` int(10) NOT NULL DEFAULT '0',
//        `money` int(10) NOT NULL DEFAULT '0' COMMENT '包括个人充值和打赏',
//        `available_money` int(10) NOT NULL DEFAULT '0',
//        `points` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '积分',
//        `available_points` int(10) NOT NULL DEFAULT '0',
//        `changed_by` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `description` varchar(80) NOT NULL DEFAULT '',
//        `data` varchar(120) NOT NULL DEFAULT '',
//        `timestamp` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`log_id`),
//        KEY `user_id` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;