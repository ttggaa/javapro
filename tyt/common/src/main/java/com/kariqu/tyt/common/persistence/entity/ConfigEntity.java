package com.kariqu.tyt.common.persistence.entity;


import javax.persistence.*;

@Entity
@Table(name = "tyt_config")
public class ConfigEntity {

    public static final int ID_MISSION_RESET = 1;   // 任务重置
    public static final int ID_RANK_RESET = 2;      // 排行榜重置时间
    public static final int ID_REGISTER_COINS = 3;  // 注册金币
    public static final int ID_DIAN_GE_COST = 4;    // 点歌消耗金币

    public ConfigEntity() {
        this.id = 0;
        this.value = "";
        this.clientUse = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "client_use", updatable = false, nullable = false)
    private int clientUse;

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getClientUse() {
        return clientUse;
    }
}
