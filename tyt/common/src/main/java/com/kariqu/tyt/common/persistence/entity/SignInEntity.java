package com.kariqu.tyt.common.persistence.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "tyt_sign_in")
public class SignInEntity {
    public static final int MAX = 7;

    public SignInEntity() {
        this.id = 0;
        this.coins = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "coins", updatable = false, nullable = false)
    private int coins;

    public int getId() {
        return id;
    }

    public int getCoins() {
        return coins;
    }
}
