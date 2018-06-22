package com.kariqu.tyt.common.persistence.entity;


import javax.persistence.*;

@Entity
@Table(name = "tyt_mission")
public class MissionEntity {
    public static final int TYPE_PAI_JIU        = 1;   // 派酒
    public static final int TYPE_NPC_PAI_JIU    = 2;   // npc派酒
    public static final int TYPE_DIAN_GE        = 3;   // 点歌
    public static final int TYPE_REBIRTH        = 4;    // 复活
    public static final int TYPE_BAR            = 5;    // 派酒类型

    public static final int NPC_TUBABA        = 100;  // 兔爸爸
    public static final int NPC_SHUMIAO       = 101;  // 树苗
    //public static final int NPC_SHULAN        = 102;  // 树懒
    //public static final int NPC_XIAOHUANXIONG = 103;  // 小浣熊
    public static final int NPC_TUMEIMEI      = 104;  // 兔妹妹
    public static final int NPC_HULI          = 105;  // 狐狸
    public static final int NPC_HUABAO        = 106;  // 花豹

    public static final int BAR_WU_JIAO_BEI   = 200;    // 无脚杯
    public static final int BAR_GAO_JIAO_BEI  = 201;    // 高脚杯
    public static final int BAR_PING_ZHUANG_JIU = 202;  // 瓶装酒
    public static final int BAR_ZA_PI         = 203;    // 扎啤
    public static final int BAR_DA_TONG_JIU   = 204;    // 大桶酒

    public MissionEntity() {
        this.missionId = 0;
        this.title = "";
        this.rewardCoin = 0;
        this.goal = 0;
        this.type = 0;
        this.attr1 = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id", updatable = false, nullable = false)
    private int missionId;

    @Column(name = "title", updatable = false, nullable = false)
    private String title;

    @Column(name = "reward_coin", updatable = false, nullable = false)
    private int rewardCoin;

    @Column(name = "goal", updatable = false, nullable = false)
    private int goal;

    @Column(name = "type", updatable = false, nullable = false)
    private int type;

    @Column(name = "attr1", updatable = false, nullable = false)
    private int attr1;

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRewardCoin() {
        return rewardCoin;
    }

    public void setRewardCoin(int rewardCoin) {
        this.rewardCoin = rewardCoin;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getType() {
        return type;
    }

    public int getAttr1() {
        return attr1;
    }
}
