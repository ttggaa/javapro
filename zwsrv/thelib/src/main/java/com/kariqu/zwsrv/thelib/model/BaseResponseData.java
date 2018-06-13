package com.kariqu.zwsrv.thelib.model;

import lombok.Data;

/**
 * Created by simon on 5/12/16.
 */

@Data
public abstract class BaseResponseData {

    protected int code;
    protected String msg;

    public BaseResponseData() {
        this.code=0;
        this.msg="";
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

}
