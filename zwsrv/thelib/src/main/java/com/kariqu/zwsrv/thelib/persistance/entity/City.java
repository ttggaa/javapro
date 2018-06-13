package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by simon on 25/11/16.
 */
@Entity
@Table(name="zww_city")
public class City extends BaseEntity {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int cityId;

    private String name;

    @Column(name="parent_id", nullable = false)
    private int parentId;

    @Column(name="short_name", nullable = false)
    private String shortName;

    private int level;

    @Column(name="city_code", nullable = false)
    private String cityCode;

    @Column(name="zip_code", nullable = false)
    private String zipCode;

    private String pinyin;

    private int status;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


//DROP TABLE IF EXISTS `zww_city`;
//        CREATE TABLE `zww_city` (
//        `id` int(7) NOT NULL COMMENT '主键',
//        `name` varchar(40) DEFAULT NULL COMMENT '省市区名称',
//        `parent_id` int(7) DEFAULT NULL COMMENT '上级ID',
//        `short_name` varchar(40) DEFAULT NULL COMMENT '简称',
//        `level` tinyint(2) DEFAULT NULL COMMENT '级别:0,中国；1，省分；2，市；3，区、县',
//        `city_code` varchar(7) DEFAULT NULL COMMENT '城市代码',
//        `zip_code` varchar(7) DEFAULT NULL COMMENT '邮编',
//        `lng` varchar(20) DEFAULT NULL COMMENT '经度',
//        `lat` varchar(20) DEFAULT NULL COMMENT '纬度',
//        `pinyin` varchar(40) DEFAULT NULL COMMENT '拼音',
//        `status` enum('0','1') DEFAULT '1'
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;