package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 26/11/17.
 */

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_coins")
public class Coins extends TimedEntity {

    @Id
    @Column(name="coins_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int coinsId;

    @Column(name="unit_amount", nullable = false)
    private int unitAmount;

    @Column(name="unit_coins", nullable = false)
    private int unitCoins;

    @Column(name="is_promotion", nullable = false)
    private int isPromotion;

    @Column(name="sort", nullable = false)
    private int sort;

    @Column(name="valid", nullable = false)
    private int valid;

    @Column(name="creator_id", nullable = false)
    private int creatorId;

    @Column(name="creator_role", nullable = false)
    private int creatorRole;

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

    public int getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
    }

    public int getUnitCoins() {
        return unitCoins;
    }

    public void setUnitCoins(int unitCoins) {
        this.unitCoins = unitCoins;
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

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getCreatorRole() {
        return creatorRole;
    }

    public void setCreatorRole(int creatorRole) {
        this.creatorRole = creatorRole;
    }
}


//CREATE TABLE `zww_coins` (
//        `coins_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
//        `unit_amount` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '精确到分100',
//        `unit_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '10',
//        `is_promotion` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否促销,批量购买',
//        `sort` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `valid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `creator_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `creator_role` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`coins_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;

