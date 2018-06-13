package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 26/11/17.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_coins_order")
public class CoinsOrder extends VersionedTimedEntity {

    public CoinsOrder() {
        orderSN="";
        orderSubject="";
        orderDesc="";
    }

    @Id
    @Column(name="order_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int orderId;

    @Column(name="order_sn", nullable = false)
    private String orderSN;

    @Column(name="order_subject", nullable = false)
    private String orderSubject;

    @Column(name="order_desc", nullable = false)
    private String orderDesc;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="coins_id", nullable = false)
    private int coinsId;

    @Column(name="unit_amount", nullable = false)
    private int unitAmount;

    @Column(name="unit_coins", nullable = false)
    private int unitCoins;

    @Column(name="is_promotion", nullable = false)
    private int isPromotion;

    @Column(name="total_amount", nullable = false)
    private int totalAmount;

    @Column(name="coins", nullable = false)
    private int coins;

    @Column(name="is_paid", nullable = false)
    private int isPaid;

    @Column(name="billing_type", nullable = false)
    private int billingType;

    @Column(name="billing_id", nullable = false)
    private int billingId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getOrderSubject() {
        return orderSubject;
    }

    public void setOrderSubject(String orderSubject) {
        this.orderSubject = orderSubject;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoinsId() {
        return coinsId;
    }

    public void setCoinsId(int coinsId) {
        this.coinsId = coinsId;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(int unitAmount) {
        this.unitAmount = unitAmount;
    }

    public int getUnitCoins() {
        return unitCoins;
    }

    public void setUnitCoins(int unitCoins) {
        this.unitCoins = unitCoins;
    }

    public int getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public int getBillingType() {
        return billingType;
    }

    public void setBillingType(int billingType) {
        this.billingType = billingType;
    }

    public int getBillingId() {
        return billingId;
    }

    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }
}

//CREATE TABLE `zww_coins_order` (
//        `order_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付记录自增id',
//        `order_sn` varchar(64) NOT NULL DEFAULT '' COMMENT '临时支付号ID',
//        `order_subject` varchar(120) NOT NULL DEFAULT '' COMMENT '订单名称',
//        `order_desc` varchar(200) NOT NULL DEFAULT '' COMMENT '详细描述',
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `coins_id` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `unit_amount` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'unit金额,精确到分100',
//        `unit_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'unit金币数,10',
//        `is_promotion` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否批量购买',
//        `total_amount` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '总金额,精确到分',
//        `coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '购买金币数',
//        `is_paid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否已支付,0否;1是',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`order_id`),
//        UNIQUE KEY `order_sn` (`order_sn`),
//        KEY `user_id` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;
