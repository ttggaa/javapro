package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_card")
public class Card extends BaseEntity {
    @Id
    @Column(name="card_type", nullable = false)
    private int cardType;

    @Column(name="card_subject", nullable = false)
    private String cardSubject;

    @Column(name="card_desc", nullable = false)
    private String cardDesc;

    @Column(name="rmb", nullable = false)
    private int rmb;

    @Column(name="duration", nullable = false)
    private long duration;

    @Column(name="immediate_coins", nullable = false)
    private int immediateCoins;

    @Column(name="extra_coins", nullable = false)
    private int extraCoins;

    @Column(name="immediate_points", nullable = false)
    private int immediatePoints;

    @Column(name="extra_points", nullable = false)
    private int extraPoints;

    @Column(name="can_buy", nullable = false)
    private int canBuy;

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getImmediateCoins() {
        return immediateCoins;
    }

    public void setImmediateCoins(int immediateCoins) {
        this.immediateCoins = immediateCoins;
    }

    public int getExtraCoins() {
        return extraCoins;
    }

    public void setExtraCoins(int extraCoins) {
        this.extraCoins = extraCoins;
    }

    public int getImmediatePoints() {
        return immediatePoints;
    }

    public void setImmediatePoints(int immediatePoints) {
        this.immediatePoints = immediatePoints;
    }

    public int getExtraPoints() {
        return extraPoints;
    }

    public void setExtraPoints(int extraPoints) {
        this.extraPoints = extraPoints;
    }

    public String getCardSubject() {
        return cardSubject;
    }

    public void setCardSubject(String cardSubject) {
        this.cardSubject = cardSubject;
    }

    public int getRmb() {
        return rmb;
    }

    public int getCanBuy() {
        return canBuy;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    public void setRmb(int rmb) {
        this.rmb = rmb;
    }

    public void setCanBuy(int canBuy) {
        this.canBuy = canBuy;
    }
}
