package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.persistance.entity.RoomType;

public class RoomTypeInfo {
    private int roomType;
    private String typeName;

    public RoomTypeInfo(RoomType rt)
    {
        this.roomType = rt.getRoomType();
        this.typeName = rt.getTypeName();
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
}
