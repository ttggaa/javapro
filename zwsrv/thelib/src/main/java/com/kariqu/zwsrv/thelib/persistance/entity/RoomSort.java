package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_room_sort")
public class RoomSort extends BaseEntity {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name="sort_group", nullable = false)
    private int sortGroup;

    @Column(name="sort_value", nullable = false)
    private int sortValue;

    @Column(name="room_id", nullable = false)
    private int roomId;

    @Column(name="is_valid", nullable = false)
    private int isValid;

    public RoomSort()
    {
        this.id = 0;
        this.sortGroup = 0;
        this.sortValue = 0;
        this.roomId = 0;
        this.isValid = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSortGroup() {
        return sortGroup;
    }

    public void setSortGroup(int sortGroup) {
        this.sortGroup = sortGroup;
    }

    public int getSortValue() {
        return sortValue;
    }

    public void setSortValue(int sortValue) {
        this.sortValue = sortValue;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }
}
