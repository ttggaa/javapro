package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import com.kariqu.zwsrv.thelib.util.LocalDateTimeUtils;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_user_card")
public class UserCard extends VersionedTimedEntity {

    public UserCard()
    {
        //signinDateTime = LocalDateTimeUtils.localDateTimeToDate(LocalDateTime.now());
        rewardDatetime = new Date(0);
        expiredDatetime = new Date(0);
    }

    @Id
    @Column(name = "card_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int cardId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "card_type", nullable = false)
    private int cardType;

    @Column(name = "reward_datetime")
    private Date rewardDatetime;

    @Column(name = "expired_datetime")
    private Date expiredDatetime;

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public Date getRewardDatetime() {
        return rewardDatetime;
    }

    public void setRewardDatetime(Date rewardDatetime) {
        this.rewardDatetime = rewardDatetime;
    }

    public Date getExpiredDatetime() {
        return expiredDatetime;
    }

    public void setExpiredDatetime(Date expiredDatetime) {
        this.expiredDatetime = expiredDatetime;
    }
}
