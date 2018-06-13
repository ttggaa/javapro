package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_charge")
public class Charge extends BaseEntity {

    @Id
    @Column(name="charge_id", nullable = false)
    private int chargeId;

    @Column(name="charge_type", nullable = false)
    private int chargeType;

    @Column(name="charge_subject", nullable = false)
    private String chargeSubject;

    @Column(name="charge_desc", nullable = false)
    private String chargeDesc;

    @Column(name="rmb", nullable = false)
    private int rmb;

    @Column(name="basic_coins", nullable = false)
    private int basicCoins;

    @Column(name="extra_coins", nullable = false)
    private int extraCoins;

    @Column(name="left_corner_mark", nullable = false)
    private int leftCornerMark;

    @Column(name="right_corner_mark", nullable = false)
    private int rightCornerMark;

    @Column(name="duration", nullable = false)
    private long duration;

    public int totalCoins()
    {
        return getBasicCoins() + getExtraCoins();
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public int getRmb() {
        return rmb;
    }

    public void setRmb(int rmb) {
        this.rmb = rmb;
    }

    public int getBasicCoins() {
        return basicCoins;
    }

    public void setBasicCoins(int basicCoins) {
        this.basicCoins = basicCoins;
    }

    public int getExtraCoins() {
        return extraCoins;
    }

    public void setExtraCoins(int extraCoins) {
        this.extraCoins = extraCoins;
    }

    public int getLeftCornerMark() {
        return leftCornerMark;
    }

    public void setLeftCornerMark(int leftCornerMark) {
        this.leftCornerMark = leftCornerMark;
    }

    public int getRightCornerMark() {
        return rightCornerMark;
    }

    public void setRightCornerMark(int rightCornerMark) {
        this.rightCornerMark = rightCornerMark;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getChargeSubject() {
        return chargeSubject;
    }

    public void setChargeSubject(String chargeSubject) {
        this.chargeSubject = chargeSubject;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }
}


//    CREATE TABLE `zww_charge` (
//        `charge_id` int(11) NOT NULL,
//        `type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '类型',
//        `rmb` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '需要人民币(分)',
//        `basic_coins` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '充值获得基础金币',
//        `extra_coins` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '充值获得额外金币',
//        `left_corner_mark` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '左角标0-没有',
//        `right_corner_mark` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '右角标，0-没有',
//        `valid` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '受否有效',
//        `order_by` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '排序显示',
//        PRIMARY KEY (`charge_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


