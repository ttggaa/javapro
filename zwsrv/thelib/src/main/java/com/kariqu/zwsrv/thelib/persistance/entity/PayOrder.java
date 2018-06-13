package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 26/11/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_pay_order")
public class PayOrder extends VersionedTimedEntity {

    public PayOrder() {
        paySN="";
        requestBiz="";
        orderSN="";
        orderSubject="";
        orderDesc="";
        payReqParams="";
        notifyData="";
        tradeNo="";
        tradeStatus="";
        buyerEmail="";
        channel = "";
    }

    @Id
    @Column(name="pay_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int payId;

    @Column(name="pay_sn", nullable = false)
    private String paySN;

    @Column(name="request_biz", nullable = false)
    private String requestBiz;

    @Column(name="order_id", nullable = false)
    private int orderId;

    @Column(name="order_sn", nullable = false)
    private String orderSN;

    @Column(name="order_subject", nullable = false)
    private String orderSubject;

    @Column(name="order_desc", nullable = false)
    private String orderDesc;

    @Column(name="pay_way", nullable = false)
    private String payWay;


    @Column(name="pay_on", nullable = false)
    private int payOn;

    @Column(name="total_amount", nullable = false)
    private int totalAmount;

    @Column(name="is_paid", nullable = false)
    private int isPaid;

    @Column(name="pay_req_params", nullable = false)
    private String payReqParams;

    @Column(name="notify_time", nullable = false)
    private long notifyTime;

    @Column(name="notify_data", nullable = false)
    private String notifyData;

    @Column(name="trade_no", nullable = false)
    private String tradeNo;

    @Column(name="trade_status", nullable = false)
    private String tradeStatus;

    @Column(name="buyer_email", nullable = false)
    private String buyerEmail;

    @Column(name="remain_in_millis", nullable = false)
    private long remainInMillis;

    @Column(name="channel", nullable = false)
    private String channel;

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public String getPaySN() {
        return paySN;
    }

    public void setPaySN(String paySN) {
        this.paySN = paySN;
    }

    public String getRequestBiz() {
        return requestBiz;
    }

    public void setRequestBiz(String requestBiz) {
        this.requestBiz = requestBiz;
    }

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

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public int getPayOn() {
        return payOn;
    }

    public void setPayOn(int payOn) {
        this.payOn = payOn;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public String getPayReqParams() {
        return payReqParams;
    }

    public void setPayReqParams(String payReqParams) {
        this.payReqParams = payReqParams;
    }

    public long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyData() {
        return notifyData;
    }

    public void setNotifyData(String notifyData) {
        this.notifyData = notifyData;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public long getRemainInMillis() {
        return remainInMillis;
    }

    public void setRemainInMillis(long remainInMillis) {
        this.remainInMillis = remainInMillis;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
//
//CREATE TABLE `zww_pay_order` (
//        `pay_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付记录自增id',
//        `pay_sn` varchar(64) NOT NULL DEFAULT '' COMMENT '临时支付号ID',
//        `request_biz` varchar(32) NOT NULL DEFAULT '' COMMENT '支付请求业务来源',
//        `order_id` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `order_sn` varchar(64) NOT NULL DEFAULT '' COMMENT '临时支付号ID',
//        `order_subject` varchar(255) NOT NULL DEFAULT '' COMMENT '订单名称',
//        `order_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '详细描述',
//        `pay_way` varchar(32) NOT NULL DEFAULT '' COMMENT '支付方式',
//        `pay_on` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '支付入口',
//        `total_amount` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '支付金额,精确到分',
//        `is_paid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否已支付,0否;1是',
//        `pay_req_params` text NOT NULL COMMENT '支付所生成的请求信息',
//        `notify_time` bigint(20) DEFAULT NULL COMMENT '通知回调时间',
//        `notify_data` text NOT NULL COMMENT '支付后回调时的详细信息',
//        `trade_no` varchar(64) NOT NULL DEFAULT '' COMMENT '支付宝交易号',
//        `trade_status` varchar(64) NOT NULL DEFAULT '' COMMENT '支付宝返回交易状态',
//        `buyer_email` varchar(120) NOT NULL DEFAULT '' COMMENT '支付宝支付则为买家支付宝帐号',
//        `remain_in_millis` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`pay_id`),
//        UNIQUE KEY `pay_sn` (`pay_sn`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;
//
