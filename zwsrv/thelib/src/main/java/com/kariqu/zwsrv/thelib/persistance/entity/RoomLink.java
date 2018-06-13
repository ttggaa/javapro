package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 09/12/17.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_room_link")
public class RoomLink extends TimedEntity {


    @Id
    @Column(name="room_link_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int roomLinkId;

    @Column(name="parent_room_id", nullable = false)
    private int parentRoomId;

    @Column(name="room_id", nullable = false)
    private int roomId;

    @Column(name="sort", nullable = false)
    private int sort;

    public int getRoomLinkId() {
        return roomLinkId;
    }

    public void setRoomLinkId(int roomLinkId) {
        this.roomLinkId = roomLinkId;
    }

    public int getParentRoomId() {
        return parentRoomId;
    }

    public void setParentRoomId(int parentRoomId) {
        this.parentRoomId = parentRoomId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}


//CREATE TABLE `zww_room_link` (
//        `room_link_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `parent_room_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `room_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `sort` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `is_normal` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否处于正常状态，zww_room:is_online,is_breakdown',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`room_link_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;

