package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 26/11/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_config")
public class Config extends TimedEntity {

    public Config() {
        data="";
    }

    @Id
    @Column(name="config_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int configId;

    @Column(name="module", nullable = false)
    private String module;

    @Column(name="type", nullable = false)
    private String type;

    @Column(name="version", nullable = false)
    private int version;

    @Column(name="sort", nullable = false)
    private int sort;

    @Column(name="valid", nullable = false)
    private int valid;

    @Column(name="width", nullable = false)
    private int width;

    @Column(name="height", nullable = false)
    private int height;

    @Column(name="data", nullable = false)
    private String data;

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

//CREATE TABLE `zww_config` (
//        `config_id` int(10) UNSIGNED NOT NULL,
//        `module` varchar(64) NOT NULL DEFAULT '',
//        `type` varchar(64) NOT NULL DEFAULT '',
//        `version` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `sort` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `width` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `height` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `valid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
//        `data` text NOT NULL,
//        `updatetime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`config_id`)
//        ) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;
