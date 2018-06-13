package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by simon on 4/26/16.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_user_count")
public class UserCount extends VersionedTimedEntity {

    public UserCount() {

    }

    @Id
    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="new_notices", nullable = false)
    private int newNotices;

    @Column(name="platform_notices", nullable = false)
    private int platformNotices;

    @Column(name="total_grabs", nullable = false)
    private int totalGrabs;

    @Column(name="success_grabs", nullable = false)
    private int successGrabs;

    @Column(name="delivery_orders", nullable = false)
    private int deliveryOrders;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNewNotices() {
        return newNotices;
    }

    public void setNewNotices(int newNotices) {
        this.newNotices = newNotices;
    }

    public int getPlatformNotices() {
        return platformNotices;
    }

    public void setPlatformNotices(int platformNotices) {
        this.platformNotices = platformNotices;
    }

    public int getTotalGrabs() {
        return totalGrabs;
    }

    public void setTotalGrabs(int totalGrabs) {
        this.totalGrabs = totalGrabs;
    }

    public int getSuccessGrabs() {
        return successGrabs;
    }

    public void setSuccessGrabs(int successGrabs) {
        this.successGrabs = successGrabs;
    }

    public int getDeliveryOrders() {
        return deliveryOrders;
    }

    public void setDeliveryOrders(int deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
    }
}


//
//CREATE TABLE `zww_user_count` (
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `new_notices` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `platform_notices` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `total_grabs` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `success_grabs` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `delivery_orders` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;




//        `in_gaming_num` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '正在游戏中的数量，防止游戏过程中断网等异常',
