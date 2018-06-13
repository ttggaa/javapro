package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import com.sun.scenario.effect.Merge;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_merge_item_log")
public class MergeItemLog extends VersionedTimedEntity {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="item_id", nullable = false)
    private int itemId;

    @Column(name="item_name", nullable = false)
    private String itemName;

    @Column(name="item_icon", nullable = false)
    private String itemIcon;

    @Column(name="item_type", nullable = false)
    private int itemType;

    @Column(name="item_classify", nullable = false)
    private int itemClassify;

    public MergeItemLog()
    {
        this.id = 0;
        this.userId = 0;
        this.itemId = 0;
        this.itemName = "";
        this.itemIcon = "";
        this.itemType = 0;
        this.itemClassify = 0;
    }

    public static MergeItemLog create(UserItem ui)
    {
        MergeItemLog mergeItemLog = new MergeItemLog();
        mergeItemLog.setUserId(ui.getUserId());
        mergeItemLog.setItemId(ui.getItemId());
        mergeItemLog.setItemName(ui.getItemName());
        mergeItemLog.setItemIcon(ui.getItemIcon());
        mergeItemLog.setItemType(ui.getItemType());
        mergeItemLog.setItemClassify(ui.getItemClassify());
        return mergeItemLog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}
