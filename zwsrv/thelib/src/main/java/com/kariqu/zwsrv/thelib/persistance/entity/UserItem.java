package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_user_item")
public class UserItem extends VersionedTimedEntity {

    public static int EXPRESS_STATUS_UNEXPRESS = 0;         // 未快递
    public static int EXPRESS_STATUS_WAIT_EXPRESS = 1;      // 未发货
    public static int EXPRESS_STATUS_EXPRESSED = 2;         // 邮寄中
    public static int EXPRESS_STATUS_SIGN = 3;              // 已经签收

    public static int EXCHANGE_HF_STATUS_UNEXCHANGE = 0;    // 未兑换话费
    public static int EXCHANGE_HF_STATUS_PROCESSING = 1;    // 正在处理
    public static int EXCHANGE_HF_STATUS_FINISHED = 2;      // 兑换完成

    public static int EXCHANGE_COINS_STATUS_UNEXCHANGE = 0; // 未兑换金币
    public static int EXCHANGE_COINS_STATUS_EXCHANGED = 1;  // 已经兑换完成

    @Id
    @Column(name="item_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int itemId;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="item_name", nullable = false)
    private String itemName;

    @Column(name="item_icon", nullable = false)
    private String itemIcon;

    @Column(name="item_type", nullable = false)
    private int itemType;

    @Column(name="item_classify", nullable = false)
    private int itemClassify;

    @Column(name="express_status", nullable = false)
    private int expressStatus;

    @Column(name="express_tm", nullable = false)
    private Date expressTm;

    @Column(name="order_id", nullable = false)
    private int orderId;

    @Column(name="originType", nullable = false)
    private int originType;

    @Column(name="originValue", nullable = false)
    private int originValue;

    @Column(name="exchange_hf_status", nullable = false)
    private int exchangeHuaFeiStatus;

    @Column(name="exchange_hf_tm", nullable = false)
    private Date exchangeHuaFeiTm;

    @Column(name="exchange_hf_mobile", nullable = false)
    private String exchangeHuaFeiMobile;

    @Column(name="exchange_hf_num", nullable = false)
    private int exchangeHuaFeiNum;

    @Column(name="exchange_coins_status", nullable = false)
    private int exchangeCoinsStatus;

    @Column(name="exchange_coins_num", nullable = false)
    private int exchangeCoinsNum;

    @Column(name="is_valid", nullable = false)
    private int isValid;

    public UserItem()
    {
        this.itemId = 0;
        this.userId = 0;
        this.itemName = "";
        this.itemIcon = "";
        this.itemType = 0;
        this.itemClassify = 0;

        this.originType = 0;
        this.originValue = 0;

        this.expressStatus = 0;
        this.expressTm = new Date(0);
        this.orderId = 0;

        this.exchangeHuaFeiStatus = 0;
        this.exchangeHuaFeiTm = new Date(0);
        this.exchangeHuaFeiMobile = "";
        this.exchangeHuaFeiNum = 0;

        this.exchangeCoinsStatus = 0;
        this.exchangeCoinsNum = 0;

        this.isValid = 1;
    }

    public static UserItem create(Item item, int userId, int origin_type)
    {
        return create(item, userId, origin_type, 0);
    }

    public static UserItem create(Item item, int userId, int origin_type, int origin_value)
    {
        UserItem user_item = new UserItem();
        user_item.setUserId(userId);
        user_item.setItemName(item.getItemName());
        user_item.setItemIcon(item.getItemIcon());
        user_item.setItemType(item.getItemType());
        user_item.setItemClassify(item.getItemClassify());

        user_item.setOriginType(origin_type);
        user_item.setOriginValue(origin_value);
        return user_item;
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

    public int getExchangeHuaFeiStatus() {
        return exchangeHuaFeiStatus;
    }

    public void setExchangeHuaFeiStatus(int exchangeHuaFeiStatus) {
        this.exchangeHuaFeiStatus = exchangeHuaFeiStatus;
    }

    public int getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(int expressStatus) {
        this.expressStatus = expressStatus;
    }

    public Date getExpressTm() {
        return expressTm;
    }

    public void setExpressTm(Date expressTm) {
        this.expressTm = expressTm;
    }

    public Date getExchangeHuaFeiTm() {
        return exchangeHuaFeiTm;
    }

    public void setExchangeHuaFeiTm(Date exchangeHuaFeiTm) {
        this.exchangeHuaFeiTm = exchangeHuaFeiTm;
    }

    public String getExchangeHuaFeiMobile() {
        return exchangeHuaFeiMobile;
    }

    public void setExchangeHuaFeiMobile(String exchangeHuaFeiMobile) {
        this.exchangeHuaFeiMobile = exchangeHuaFeiMobile;
    }

    public int getOriginType() {
        return originType;
    }

    public void setOriginType(int originType) {
        this.originType = originType;
    }

    public int getOriginValue() {
        return originValue;
    }

    public void setOriginValue(int originValue) {
        this.originValue = originValue;
    }

    public int getItemClassify() {
        return itemClassify;
    }

    public void setItemClassify(int itemClassify) {
        this.itemClassify = itemClassify;
    }

    public int getIsValid() {
        return isValid;
    }

    public int getExchangeHuaFeiNum() {
        return exchangeHuaFeiNum;
    }

    public void setExchangeHuaFeiNum(int exchangeHuaFeiNum) {
        this.exchangeHuaFeiNum = exchangeHuaFeiNum;
    }

    public int getExchangeCoinsStatus() {
        return exchangeCoinsStatus;
    }

    public void setExchangeCoinsStatus(int exchangeCoinsStatus) {
        this.exchangeCoinsStatus = exchangeCoinsStatus;
    }

    public int getExchangeCoinsNum() {
        return exchangeCoinsNum;
    }

    public void setExchangeCoinsNum(int exchangeCoinsNum) {
        this.exchangeCoinsNum = exchangeCoinsNum;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
