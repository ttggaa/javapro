package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;

/**
 * Created by simon on 10/12/17.
 */
public class ReportRoomErrorInfo extends BaseModel {

    private int roomId; //
    private int isBreakdown;
    private int errorCode;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getIsBreakdown() {
        return isBreakdown;
    }

    public void setIsBreakdown(int isBreakdown) {
        this.isBreakdown = isBreakdown;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
