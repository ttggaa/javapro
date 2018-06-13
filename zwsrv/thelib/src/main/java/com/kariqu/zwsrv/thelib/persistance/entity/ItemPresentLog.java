package com.kariqu.zwsrv.thelib.persistance.entity;


import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_item_present_log")
public class ItemPresentLog extends BaseEntity {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="item_id", nullable = false)
    private int itemId;

    @Column(name="item_type", nullable = false)
    private int itemType;

    @Column(name="itemClassify", nullable = false)
    private int itemClassify;

    @Column(name="item_name", nullable = false)
    private String itemName;

    @Column(name="item_icon", nullable = false)
    private String itemIcon;

    @Column(name="web_name", nullable = false)
    private String webName;

    @Column(name="remark", nullable = false)
    private String remark;

    public ItemPresentLog()
    {
        this.id = 0;
        this.itemId = 0;
        this.userId = 0;
        this.itemType = 0;
        this.itemClassify = 0;
        this.itemName = "";
        this.itemIcon = "";
        this.webName = "";
        this.remark = "";
    }

    public static ItemPresentLog create(Item item, int itemId, int userId, String webName, String remark)
    {
        ItemPresentLog temp = new ItemPresentLog();
        temp.setUserId(userId);
        temp.setItemId(itemId);
        temp.setItemType(item.getItemType());
        temp.setItemClassify(item.getItemClassify());
        temp.setItemName(item.getItemName());
        temp.setItemIcon(item.getItemIcon());
        temp.setWebName(webName);
        temp.setRemark(remark);
        return temp;
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

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
