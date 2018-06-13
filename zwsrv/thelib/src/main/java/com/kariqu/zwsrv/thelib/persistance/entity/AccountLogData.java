package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.model.BaseModel;

/**
 * Created by simon on 09/12/17.
 */
public class AccountLogData extends BaseModel {

    private int idValue;
    private String name;
    private String imgUrl;

    public AccountLogData() {
        name="";
        imgUrl="";
    }

    public AccountLogData(int idValue,String name,String imgUrl) {
        this.idValue=idValue;
        this.name=name!=null?name:"";
        this.imgUrl=imgUrl!=null?imgUrl:"";
    }

    public int getIdValue() {
        return idValue;
    }

    public void setIdValue(int idValue) {
        this.idValue = idValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
