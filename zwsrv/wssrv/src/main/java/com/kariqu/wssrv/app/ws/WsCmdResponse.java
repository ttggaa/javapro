package com.kariqu.wssrv.app.ws;

import com.kariqu.wssrv.app.error.ErrorCode;

/**
 * Created by simon on 10/05/2018.
 */
public class WsCmdResponse {
    private int code;
    private String msg;
    private int type;

    public WsCmdResponse(WsCmdType type){
        this.code = ErrorCode.ERROR_SUCCESS.getCode();
        this.msg = ErrorCode.ERROR_SUCCESS.getMsg();
        this.type = type.getType();
    }

    public WsCmdResponse(WsCmdType type, ErrorCode.ErrorResult result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
        this.type = type.getType();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
