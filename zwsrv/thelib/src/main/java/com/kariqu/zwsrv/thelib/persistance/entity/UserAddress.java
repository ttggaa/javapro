package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 03/11/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_user_address")
public class UserAddress extends TimedEntity {

    public UserAddress() {
        addressName="";
        consignee="";
        address="";
        mobile="";
        remarks="";
    }

    @Id
    @Column(name="address_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int addressId;

    @Column(name="address_name", nullable = false)
    private String addressName;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="consignee", nullable = false)
    private String consignee;

    @Column(name="country", nullable = false)
    private int country;

    @Column(name="province", nullable = false)
    private int province;

    @Column(name="city", nullable = false)
    private int city;

    @Column(name="district", nullable = false)
    private int district;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="mobile", nullable = false)
    private String mobile;

    @Column(name="remarks", nullable = false)
    private String remarks;

    @Column(name="sort", nullable = false)
    private int sort;

    @Column(name="is_default", nullable = false)
    private int isDefault;

    @Column(name="deleted", nullable = false)
    private int deleted;


    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}

//CREATE TABLE `zww_user_address` (
//        `address_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `address_name` varchar(80) NOT NULL DEFAULT '',
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `consignee` varchar(60) NOT NULL DEFAULT '',
//        `country` int(10) NOT NULL DEFAULT '0',
//        `province` int(10) NOT NULL DEFAULT '0',
//        `city` int(10) NOT NULL DEFAULT '0',
//        `district` int(10) NOT NULL DEFAULT '0',
//        `address` varchar(120) NOT NULL DEFAULT '',
//        `mobile` varchar(60) NOT NULL DEFAULT '',
//        `remarks` varchar(120) NOT NULL DEFAULT '',
//        `sort` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
//        `is_default` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
//        `deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`address_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;