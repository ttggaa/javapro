package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by simon on 03/11/17.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_delivery_order")
public class DeliveryOrder extends VersionedTimedEntity {

//    `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未发货,1已发货配送中,2已收货',

    public static final int DELIVERY_ORDER_SHIPPING_STATUS_INITIAL = 0;
    public static final int DELIVERY_ORDER_SHIPPING_STATUS_DELIVERYED = 0;

    public static final int DELIVERY_ORDER_SIGNIN_STATUS_INITIAL = 0;
    public static final int DELIVERY_ORDER_SIGNIN_STATUS_SIGNED = 0;

    //        `shipping_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未发货,1已发货配送中',
    //        `signin_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0' COMMENT '签收时间',
//        `signin_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0签收,1已签收',


    public DeliveryOrder() {
        orderSN="";
        shippingName="";
        shippingCode="";
        shippingOrderSN="";
        consignee="";
        address="";
        mobile="";
        remarks="";
        postscript="";
    }

    @Id
    @Column(name="order_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int orderId;

    @Column(name="order_sn", nullable = false)
    private String orderSN;

    @Column(name="goods_num", nullable = false)
    private int goodsNum;

    @Column(name="shipping_id", nullable = false)
    private int shippingId;

    @Column(name="shipping_name", nullable = false)
    private String shippingName;

    @Column(name="shipping_code", nullable = false)
    private String shippingCode;

    @Column(name="shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name="shipping_points", nullable = false)
    private int shippingPoints;

    @Column(name="shipping_coins", nullable = false)
    private int shippingCoins;

    @Column(name="is_free_shipping", nullable = false)
    private int isFreeShipping;

    @Column(name="pay_way", nullable = false)
    private int payWay;

    @Column(name="pay_status", nullable = false)
    private int payStatus;

    @Column(name="points_paid", nullable = false)
    private int pointsPaid;

    @Column(name="coins_paid", nullable = false)
    private int coinsPaid;

    @Column(name="shipping_order_sn", nullable = false)
    private String shippingOrderSN;

    @Column(name="shipping_time", nullable = false)
    private long shippingTime;

    @Column(name="shipping_status", nullable = false)
    private int shippingStatus;

    @Column(name="signin_time", nullable = false)
    private long signinTime;

    @Column(name="signin_status", nullable = false)
    private long signinStatus;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="address_id", nullable = false)
    private int addressId;

    @Column(name="consignee", nullable = false)
    private String consignee;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="country", nullable = false)
    private int country;

    @Column(name="province", nullable = false)
    private int province;

    @Column(name="city", nullable = false)
    private int city;

    @Column(name="district", nullable = false)
    private int district;

    @Column(name="mobile", nullable = false)
    private String mobile;

    @Column(name="remarks", nullable = false)
    private String remarks;

    @Column(name="postscript", nullable = false)
    private String postscript;


    //    @OneToMany(cascade = { CascadeType.ALL })
//    @JoinTable(name="ref_customer_address",
//            joinColumns={ @JoinColumn(name="customer_id",referencedColumnName="id")},
//            inverseJoinColumns={@JoinColumn(name="address_id",referencedCslumnName="id")})
//    private Collection<AddressEO> addresses = new ArrayList<AddressEO>();

    //, optional = false, fetch = FetchType.LAZY
//    @OneToMany(cascade={CascadeType.ALL})
//    @JoinTable(name="DeliveryGoods",
//            joinColumns={ @JoinColumn(name="order_id",referencedColumnName="order_id")})
    @OneToMany(cascade={CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Collection<DeliveryGoods> deliveryGoodsList=new ArrayList<>();

    public Collection<DeliveryGoods> getDeliveryGoodsList() {
        return deliveryGoodsList;
    }

    public void setDeliveryGoodsList(Collection<DeliveryGoods> deliveryGoodsList) {
        this.deliveryGoodsList = deliveryGoodsList;
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

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getShippingPoints() {
        return shippingPoints;
    }

    public void setShippingPoints(int shippingPoints) {
        this.shippingPoints = shippingPoints;
    }

    public int getShippingCoins() {
        return shippingCoins;
    }

    public void setShippingCoins(int shippingCoins) {
        this.shippingCoins = shippingCoins;
    }

    public int getIsFreeShipping() {
        return isFreeShipping;
    }

    public void setIsFreeShipping(int isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPointsPaid() {
        return pointsPaid;
    }

    public void setPointsPaid(int pointsPaid) {
        this.pointsPaid = pointsPaid;
    }

    public int getCoinsPaid() {
        return coinsPaid;
    }

    public void setCoinsPaid(int coinsPaid) {
        this.coinsPaid = coinsPaid;
    }

    public String getShippingOrderSN() {
        return shippingOrderSN;
    }

    public void setShippingOrderSN(String shippingOrderSN) {
        this.shippingOrderSN = shippingOrderSN;
    }

    public long getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(long shippingTime) {
        this.shippingTime = shippingTime;
    }

    public int getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(int shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public long getSigninTime() {
        return signinTime;
    }

    public void setSigninTime(long signinTime) {
        this.signinTime = signinTime;
    }

    public long getSigninStatus() {
        return signinStatus;
    }

    public void setSigninStatus(long signinStatus) {
        this.signinStatus = signinStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
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

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }
}


//CREATE TABLE `zww_delivery_order` (
//        `order_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `order_sn` varchar(32) NOT NULL DEFAULT '',
//        `goods_num` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '货品数量',
//        `shipping_id` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '配送方式',
//        `shipping_name` varchar(80) NOT NULL DEFAULT '' COMMENT '',
//        `shipping_code` varchar(32) NOT NULL DEFAULT '' COMMENT '',
//        `shipping_fee` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '运费,开销统计用',
//        `shipping_points` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '兑换运费需要的积分',
//        `shipping_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '兑换运费需要的金币',
//        `is_free_shipping` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否免运费',
//        `pay_way` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '1.积分支付 2.金币支付',
//        `pay_status` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '运费是否已支付',
//        `points_paid` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `coins_paid` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `shipping_order_sn` varchar(64) NOT NULL DEFAULT '' COMMENT '发货订单号',
//        `shipping_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0' COMMENT '发货时间',
//        `shipping_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未发货,1已发货配送中',
//        `signin_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0' COMMENT '签收时间',
//        `signin_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0签收,1已签收',
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `address_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `consignee` varchar(80) NOT NULL DEFAULT '' COMMENT '',
//        `address` varchar(250) NOT NULL DEFAULT '' COMMENT '',
//        `country` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `province` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `city` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `district` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `mobile` varchar(60) NOT NULL DEFAULT '' COMMENT '',
//        `remarks` varchar(120) NOT NULL DEFAULT '' COMMENT '备注,来自于收货地址',
//        `postscript` varchar(120) NOT NULL DEFAULT '' COMMENT '附言',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`order_id`),
//        KEY `user_id` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;
