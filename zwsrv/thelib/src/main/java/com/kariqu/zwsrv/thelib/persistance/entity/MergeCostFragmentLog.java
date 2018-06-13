package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_merge_cost_fragment_log")
public class MergeCostFragmentLog extends VersionedTimedEntity {

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

    @Column(name="merge_item_id", nullable = false)
    private int mergeItemId;

    @Column(name="merge_tm", nullable = false)
    private Date mergeTm;

    public MergeCostFragmentLog()
    {
        this.id = 0;
        this.userId = 0;
        this.itemId = 0;
        this.itemName = "";
        this.itemIcon = "";
        this.itemType = 0;
        this.itemClassify = 0;
        this.mergeItemId = 0;
        this.mergeTm = new Date(0);
    }

    public static MergeCostFragmentLog create(UserItem user_item)
    {
        MergeCostFragmentLog mergeCostFragmentLog = new MergeCostFragmentLog();
        mergeCostFragmentLog.setUserId(user_item.getUserId());
        mergeCostFragmentLog.setItemId(user_item.getItemId());
        mergeCostFragmentLog.setItemName(user_item.getItemName());
        mergeCostFragmentLog.setItemIcon(user_item.getItemIcon());
        mergeCostFragmentLog.setItemType(user_item.getItemType());
        mergeCostFragmentLog.setItemClassify(user_item.getItemClassify());
        return mergeCostFragmentLog;
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

    public int getMergeItemId() {
        return mergeItemId;
    }

    public void setMergeItemId(int mergeItemId) {
        this.mergeItemId = mergeItemId;
    }

    public Date getMergeTm() {
        return mergeTm;
    }

    public void setMergeTm(Date mergeTm) {
        this.mergeTm = mergeTm;
    }
}
