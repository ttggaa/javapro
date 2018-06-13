package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.PlatDict;

/**
 * Created by simon on 09/01/2018.
 */
public class PlatDictInfo extends BaseModel {
    private String name; //分享app的图片地址
    private String value; //微信客服号

    public PlatDictInfo() {
        this.name="";
        this.value="";
    }

    public PlatDictInfo(PlatDict platDict) {
        this.name=platDict.getName();
        this.value=platDict.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


//获取分享的二维码和客服微信号以及二维码