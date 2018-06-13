package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.thelib.persistance.entity.MergeItemLog;

public class MergeItemLogInfo {
    private int itemId;
    private String itemName;
    private String itemIcon;
    private int itemType;
    private int itemClassify;
    private long createtime;

    public MergeItemLogInfo(MergeItemLog itemLog)
    {
        this.itemId = itemLog.getItemId();
        this.itemName = itemLog.getItemName();
        this.itemIcon = URLHelper.fullUrl(itemLog.getItemIcon());
        this.itemType = itemLog.getItemType();
        this.itemClassify = itemLog.getItemClassify();
        this.createtime = itemLog.getCreateTime();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}
