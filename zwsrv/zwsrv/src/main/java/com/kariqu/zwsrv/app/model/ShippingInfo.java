package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Shipping;

/**
 * Created by simon on 16/12/17.
 */
public class ShippingInfo extends BaseModel {
    private int shippingId;
    private String name;
    private String code;
    private String shippingDesc;
    private int fee;
    private int points;
    private int coins;
    private int sort;

    public ShippingInfo() {
        name="";
        code="";
        shippingDesc="";
    }

    public ShippingInfo(Shipping shipping) {
        this.shippingId=shipping.getShippingId();
        this.name=shipping.getName();
        this.code=shipping.getCode();
        this.shippingDesc=shipping.getShippingDesc();
        this.fee=shipping.getFee();
        this.points=shipping.getPoints();
        this.coins=shipping.getCoins();
        this.sort=shipping.getSort();
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShippingDesc() {
        return shippingDesc;
    }

    public void setShippingDesc(String shippingDesc) {
        this.shippingDesc = shippingDesc;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
