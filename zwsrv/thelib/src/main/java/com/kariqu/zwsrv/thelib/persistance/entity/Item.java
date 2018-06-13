package com.kariqu.zwsrv.thelib.persistance.entity;


import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_item")
public class Item extends BaseEntity {
    @Id
    @Column(name="item_type", nullable = false)
    private int itemType;

    @Column(name="item_classify", nullable = false)
    private int itemClassify;

    @Column(name="item_name", nullable = false)
    private String itemName;

    @Column(name="item_icon", nullable = false)
    private String itemIcon;

    @Column(name="can_express", nullable = false)
    private int canExpress;

    @Column(name="can_exchange_hf", nullable = false)
    private int canExchangeHuaFei;

    @Column(name="exchange_hf_num", nullable = false)
    private int exchangeFhNum;

    @Column(name="can_exchange_coins", nullable = false)
    private int canExchangeCoins;

    @Column(name="exchange_coins_num", nullable = false)
    private int exchangeCoinsNum;

    @Column(name="canMerge", nullable = false)
    private int canMerge;

    @Column(name="merge_count", nullable = false)
    private int mergeCount;

    @Column(name="merge_item_type", nullable = false)
    private int mergeItemType;

    @Column(name="can_pile", nullable = false)
    private int canPile;

    @Column(name="pile_max", nullable = false)
    private int pileMax;

    public Item()
    {
        this.itemType = 0;
        this.itemClassify = 0;
        this.itemName = "";
        this.canExpress = 0;
        this.canMerge = 0;
        this.mergeCount = 0;
        this.mergeItemType = 0;
        this.canPile = 0;
        this.pileMax = 0;

        this.canExchangeHuaFei = 0;
        this.exchangeFhNum = 0;
        this.canExchangeCoins = 0;
        this.exchangeCoinsNum = 0;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCanExpress() {
        return canExpress;
    }

    public void setCanExpress(int canExpress) {
        this.canExpress = canExpress;
    }

    public int getCanMerge() {
        return canMerge;
    }

    public void setCanMerge(int canMerge) {
        this.canMerge = canMerge;
    }

    public int getMergeCount() {
        return mergeCount;
    }

    public void setMergeCount(int mergeCount) {
        this.mergeCount = mergeCount;
    }

    public int getCanPile() {
        return canPile;
    }

    public void setCanPile(int canPile) {
        this.canPile = canPile;
    }

    public int getPileMax() {
        return pileMax;
    }

    public void setPileMax(int pileMax) {
        this.pileMax = pileMax;
    }

    public int getItemClassify() {
        return itemClassify;
    }

    public void setItemClassify(int itemClassify) {
        this.itemClassify = itemClassify;
    }

    public String getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public int getCanExchangeHuaFei() {
        return canExchangeHuaFei;
    }

    public void setCanExchangeHuaFei(int canExchangeHuaFei) {
        this.canExchangeHuaFei = canExchangeHuaFei;
    }

    public int getExchangeFhNum() {
        return exchangeFhNum;
    }

    public void setExchangeFhNum(int exchangeFhNum) {
        this.exchangeFhNum = exchangeFhNum;
    }

    public int getCanExchangeCoins() {
        return canExchangeCoins;
    }

    public void setCanExchangeCoins(int canExchangeCoins) {
        this.canExchangeCoins = canExchangeCoins;
    }

    public int getExchangeCoinsNum() {
        return exchangeCoinsNum;
    }

    public void setExchangeCoinsNum(int exchangeCoinsNum) {
        this.exchangeCoinsNum = exchangeCoinsNum;
    }

    public int getMergeItemType() {
        return mergeItemType;
    }

    public void setMergeItemType(int mergeItemType) {
        this.mergeItemType = mergeItemType;
    }
}
