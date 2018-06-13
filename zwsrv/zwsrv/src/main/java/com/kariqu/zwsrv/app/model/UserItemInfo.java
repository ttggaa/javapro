package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.app.utility.Utility;
import com.kariqu.zwsrv.thelib.persistance.entity.Item;
import com.kariqu.zwsrv.thelib.persistance.entity.UserItem;

public class UserItemInfo {
    private int itemId;
    private int userId;
    private String itemName;
    private String itemIcon;
    private int itemType;
    private int itemClassify;
    private int originType;

    private int expressStatus;          // 快递状态
    //private long expressTm;             // 快递时间

    private int exchangeHfStatus;       // 兑换话费状态
    //private int exchangeHfTm;           // 兑换花费时间
    private int exchangeHf;             // 兑换了多少花费
    private String exchangeHfMobile;    // 花费手机号

    private int exchangeCoinsStatus;    // 兑换金币状态
    private int exchangeCoins;          // 兑换金币数

    private int exchangeCoinsNum;       // 可兑换金币数量
    private long storageExpiredTime;    // 寄存剩余时间
    private long createtime;            // 创建时间

    public UserItemInfo(UserItem userItem, Item item, long goodsMaxStorageTime, long currentMilliseconds) {
        this.itemId = userItem.getItemId();
        this.userId = userItem.getUserId();
        this.itemName = userItem.getItemName();
        this.itemIcon = URLHelper.fullUrl(userItem.getItemIcon());
        this.itemType = userItem.getItemType();
        this.itemClassify = userItem.getItemClassify();
        this.originType = userItem.getOriginType();

        this.expressStatus = userItem.getExpressStatus();

        this.exchangeHfStatus = userItem.getExchangeHuaFeiStatus();
        this.exchangeHfMobile = userItem.getExchangeHuaFeiMobile();
        // 未兑换，显示配置表, 兑换后显示真实兑换
        if (this.exchangeHfStatus == userItem.EXCHANGE_HF_STATUS_UNEXCHANGE) {
            this.exchangeHf = item.getExchangeFhNum();
        } else {
            this.exchangeHf = userItem.getExchangeHuaFeiNum();
        }

        this.exchangeCoinsStatus = userItem.getExchangeCoinsStatus();
        // 未兑换，显示配置表, 兑换后显示真实兑换
        if (this.exchangeCoinsStatus == UserItem.EXCHANGE_COINS_STATUS_UNEXCHANGE) {
            this.exchangeCoins = item.getExchangeCoinsNum();
        } else {
            this.exchangeCoins = item.getExchangeCoinsNum();
        }

        this.createtime = userItem.getCreateTime();

        this.storageExpiredTime = Utility.getExpiredTime(getCreatetime(), goodsMaxStorageTime, currentMilliseconds);
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

    public int getOriginType() {
        return originType;
    }

    public void setOriginType(int originType) {
        this.originType = originType;
    }

    public int getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(int expressStatus) {
        this.expressStatus = expressStatus;
    }

    public int getExchangeHfStatus() {
        return exchangeHfStatus;
    }

    public void setExchangeHfStatus(int exchangeHfStatus) {
        this.exchangeHfStatus = exchangeHfStatus;
    }

    public int getExchangeHf() {
        return exchangeHf;
    }

    public void setExchangeHf(int exchangeHf) {
        this.exchangeHf = exchangeHf;
    }

    public String getExchangeHfMobile() {
        return exchangeHfMobile;
    }

    public void setExchangeHfMobile(String exchangeHfMobile) {
        this.exchangeHfMobile = exchangeHfMobile;
    }

    public int getExchangeCoinsStatus() {
        return exchangeCoinsStatus;
    }

    public void setExchangeCoinsStatus(int exchangeCoinsStatus) {
        this.exchangeCoinsStatus = exchangeCoinsStatus;
    }

    public int getExchangeCoins() {
        return exchangeCoins;
    }

    public void setExchangeCoins(int exchangeCoins) {
        this.exchangeCoins = exchangeCoins;
    }

    public int getExchangeCoinsNum() {
        return exchangeCoinsNum;
    }

    public void setExchangeCoinsNum(int exchangeCoinsNum) {
        this.exchangeCoinsNum = exchangeCoinsNum;
    }

    public long getStorageExpiredTime() {
        return storageExpiredTime;
    }

    public void setStorageExpiredTime(long storageExpiredTime) {
        this.storageExpiredTime = storageExpiredTime;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getItemClassify() {
        return itemClassify;
    }

    public void setItemClassify(int itemClassify) {
        this.itemClassify = itemClassify;
    }
}
