package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 03/11/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_shipping")
public class Shipping extends TimedEntity {

    public Shipping() {
        name="";
        code="";
        shippingDesc="";
    }

    @Id
    @Column(name="shipping_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int shippingId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="code", nullable = false)
    private String code;

    @Column(name="description", nullable = false)
    private String shippingDesc;

    @Column(name="fee", nullable = false)
    private int fee;

    @Column(name="points", nullable = false)
    private int points;

    @Column(name="coins", nullable = false)
    private int coins;

    @Column(name="sort", nullable = false)
    private int sort;

    @Column(name="valid", nullable = false)
    private int valid;

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShippingDesc() {
        return shippingDesc;
    }

    public void setShippingDesc(String shippingDesc) {
        this.shippingDesc = shippingDesc;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }
}


//CREATE TABLE `zww_shipping` (
//        `shipping_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `name` varchar(80) NOT NULL DEFAULT '',
//        `code` varchar(80) NOT NULL DEFAULT '',
//        `description` varchar(255) NOT NULL DEFAULT '',
//        `fee` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '运费',
//        `points` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '兑换运费需要的积分',
//        `coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '兑换运费需要的金币',
//        `sort` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `valid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`shipping_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;