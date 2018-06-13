package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.UserCard;
import com.kariqu.zwsrv.thelib.persistance.service.UserCardService;

public class UserCardInfo extends BaseModel {
    private int cardType;           // 卡片类型 周卡，月卡
    private long remainSeconds;     // 剩余多少秒后可以领取
    private int coins;              // 能领取数量

    public UserCardInfo(UserCard user_card, long remainMs, int totalCoins) {
        this.cardType = user_card.getCardType();
        this.remainSeconds = (int)(remainMs/1000);
        this.coins = totalCoins;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
