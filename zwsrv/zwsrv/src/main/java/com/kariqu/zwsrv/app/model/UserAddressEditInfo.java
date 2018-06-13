package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.UserAddress;

/**
 * Created by simon on 08/12/17.
 */
public class UserAddressEditInfo extends BaseModel {

    private int addressId;
    private String consignee;
    private int province;
    private int city;
    private int district;
    private String address;
    private String mobile;
    private String remarks;

    public UserAddressEditInfo() {
        consignee="";
        address="";
        mobile="";
        remarks="";
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public UserAddress toUserAddress() {
        UserAddress userAddress = new UserAddress();
        userAddress.setAddressId(this.getAddressId());
        userAddress.setConsignee(this.getConsignee());
        userAddress.setProvince(this.getProvince());
        userAddress.setCity(this.getCity());
        userAddress.setDistrict(this.getDistrict());
        userAddress.setAddress(this.getAddress());
        userAddress.setMobile(this.getMobile()!=null?this.getMobile():"");
        userAddress.setRemarks(this.getRemarks());
        return userAddress;
    }
}
