package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.persistance.entity.ItemPresentLog;

public class ItemPresentLogInfo {
    private int id;
    private int itemId;
    private int userId;
    private int itemType;
    private int itemClassify;
    private String itemName;
    private String itemIcon;


    public ItemPresentLogInfo(ItemPresentLog presentLog)
    {
        this.id = presentLog.getId();
        this.itemId = presentLog.getItemId();
        this.userId = presentLog.getUserId();
        this.itemType = presentLog.getItemType();
        this.itemClassify = presentLog.getItemClassify();
        this.itemName = presentLog.getItemName();
        this.itemIcon = presentLog.getItemIcon();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemClassify() {
        return itemClassify;
    }

    public void setItemClassify(int itemClassify) {
        this.itemClassify = itemClassify;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }
}
