package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;

/**
 * Created by simon on 16/01/2018.
 */
public class DeliveryGoodsInfo extends BaseModel {

    private int orderId;
    private int type;
    private int idvalue;
    private String imageUrl;
    private String name;

    public DeliveryGoodsInfo(DeliveryGoods deliveryGoods) {
        this.orderId=deliveryGoods.getOrderId();
        this.type=deliveryGoods.getType();
        this.idvalue=deliveryGoods.getIdvalue();
        this.name=deliveryGoods.getName();

        this.imageUrl=URLHelper.fullUrl(deliveryGoods.getImageUrl());
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdvalue() {
        return idvalue;
    }

    public void setIdvalue(int idvalue) {
        this.idvalue = idvalue;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
