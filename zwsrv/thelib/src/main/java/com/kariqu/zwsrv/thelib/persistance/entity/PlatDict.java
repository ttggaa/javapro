package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 21/12/17.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_dict")
public class PlatDict extends TimedEntity {

    @Id
    @Column(name="dict_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int dictId;

    @Column(name="name", unique = true, nullable = false, updatable = true)
    private String name;

    @Column(name="value", nullable = false)
    private String value;


    @Column(name="is_url", nullable = false)
    private int isUrl;

    @Column(name="is_plat_config", nullable = false)
    private int isPlatConfig;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public int getIsUrl() {
        return isUrl;
    }

    public void setIsUrl(int isUrl) {
        this.isUrl = isUrl;
    }

    public int getIsPlatConfig() {
        return isPlatConfig;
    }

    public void setIsPlatConfig(int isPlatConfig) {
        this.isPlatConfig = isPlatConfig;
    }
}


//CREATE TABLE `zww_dict` (
//        `dict_id` int(10) UNSIGNED NOT NULL,
//        `name` varchar(64) NOT NULL DEFAULT '',
//        `value` varchar(255) NOT NULL DEFAULT '',
//        `updatetime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`dict_id`),
//        UNIQUE KEY (`name`)
//        ) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;

