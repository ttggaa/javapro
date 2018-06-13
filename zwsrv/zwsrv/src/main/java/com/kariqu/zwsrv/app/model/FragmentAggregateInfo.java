package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.app.service.UserItemServiceCache;
import com.kariqu.zwsrv.thelib.persistance.entity.Item;

public class FragmentAggregateInfo {

    private int itemType;
    private String itemIcon;
    private String itemName;
    private int count;          // 拥有数量
    private int mergeCount;     // 合成需要数量

    public FragmentAggregateInfo(UserItemServiceCache.FragmentAggregate p, Item item)
    {
        this.itemType = p.getItemType();
        this.count = p.getCount();
        this.itemIcon = URLHelper.fullUrl(item.getItemIcon());
        this.itemName = item.getItemName();
        this.mergeCount = item.getMergeCount();
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getMergeCount() {
        return mergeCount;
    }

    public void setMergeCount(int mergeCount) {
        this.mergeCount = mergeCount;
    }
}


