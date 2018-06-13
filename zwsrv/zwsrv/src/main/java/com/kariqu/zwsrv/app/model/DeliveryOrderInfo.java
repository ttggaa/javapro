package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by simon on 16/01/2018.
 */
public class DeliveryOrderInfo extends BaseModel {

    private int orderId;
    private String orderSN;
    private int goodsNum;
    private int shippingId;
    private String shippingName;
    private int shippingCoins;
    private int isFreeShipping;
    private int payWay;
    private int payStatus;
    private int coinsPaid;
    private String shippingOrderSN;
    private long shippingTime;
    private int shippingStatus;
    private long signinTime;
    private long signinStatus;
    private int userId;
    private int addressId;
    private String consignee;
    private String address;
    private int country;
    private int province;
    private int city;
    private int district;
    private String mobile;
    private String remarks;
    protected long updateTime;
    protected long createTime;
    List<DeliveryGoodsInfo> deliveryGoodsList;

    public DeliveryOrderInfo(DeliveryOrder deliveryOrder) {
        this.orderId=deliveryOrder.getOrderId();
        this.orderSN=deliveryOrder.getOrderSN();
        this.shippingName=deliveryOrder.getShippingName();
        this.shippingCoins=deliveryOrder.getShippingCoins();
        this.isFreeShipping=deliveryOrder.getIsFreeShipping();
        this.payWay=deliveryOrder.getPayWay();
        this.payStatus=deliveryOrder.getPayStatus();
        this.coinsPaid=deliveryOrder.getCoinsPaid();
        this.shippingOrderSN=deliveryOrder.getShippingOrderSN();
        this.shippingTime=deliveryOrder.getShippingTime();
        this.shippingStatus=deliveryOrder.getShippingStatus();
        this.signinTime=deliveryOrder.getSigninTime();
        this.signinStatus=deliveryOrder.getSigninStatus();
        this.userId=deliveryOrder.getUserId();
        this.addressId=deliveryOrder.getAddressId();
        this.consignee=deliveryOrder.getConsignee();
        this.address=deliveryOrder.getAddress();
        this.country=deliveryOrder.getCountry();
        this.province=deliveryOrder.getProvince();
        this.city=deliveryOrder.getCity();
        this.district=deliveryOrder.getDistrict();
        this.mobile=deliveryOrder.getMobile();
        this.remarks=deliveryOrder.getRemarks();
        this.updateTime=deliveryOrder.getUpdateTime();
        this.createTime=deliveryOrder.getCreateTime();
        this.deliveryGoodsList=new ArrayList<>();
        Collection<DeliveryGoods> deliveryGoodsList = deliveryOrder.getDeliveryGoodsList();
        if (deliveryGoodsList!=null) {
            deliveryGoodsList.forEach(
                    (DeliveryGoods deliveryGoods)->this.deliveryGoodsList.add(new DeliveryGoodsInfo(deliveryGoods)));
        }
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<DeliveryGoodsInfo> getDeliveryGoodsList() {
        return deliveryGoodsList;
    }

    public void setDeliveryGoodsList(List<DeliveryGoodsInfo> deliveryGoodsList) {
        this.deliveryGoodsList = deliveryGoodsList;
    }
}
