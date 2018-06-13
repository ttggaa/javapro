package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_room_type")
public class RoomType  extends BaseEntity {

    @Id
    @Column(name="room_type", nullable = false)
    private int roomType;

    @Column(name="type_name", nullable = false)
    private String typeName;

    @Column(name="sort", nullable = false)
    private int sort;

    @Column(name="is_valid", nullable = false)
    private int isValid;

    public RoomType()
    {
        this.roomType = 0;
        this.typeName = "";
        this.sort = 0;
        this.isValid = 0;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }
}
