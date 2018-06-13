package com.kariqu.wssrv.app.room;

import com.kariqu.zwsrv.thelib.persistance.entity.TbjGame;

public class CurrentPlayer {
    private TbjPlayer player;               // 当前用户
    private long dropTimestamp;             // 上次投币时间戳

    private int dropCount;              // 投币次数
    private int dropCoins;              // 投币总额
    private int rewardCount;            // 奖励次数
    private int rewardCoins;            // 奖励总额

    private TbjGame game;

    public CurrentPlayer(TbjPlayer player) {
        this.player = player;
        this.dropTimestamp = System.currentTimeMillis();
        this.dropCount = 0;
        this.dropCoins = 0;
        this.rewardCount = 0;
        this.rewardCoins = 0;
        this.game = null;
    }

    void onUpdateReward(int num) {
        this.rewardCount++;
        this.rewardCoins += num;
    }

    void onDropCoin(int num) {
        this.dropCount++;
        this.dropCoins += num;
    }


    public String userId() { return player.userId(); }

    public TbjPlayer getPlayer() { return player; }

    public long getDropTimestamp() { return dropTimestamp; }

    public void setDropTimestamp(long dropTimestamp) { this.dropTimestamp = dropTimestamp; }

    public int getDropCount() {
        return dropCount;
    }

    public void setDropCount(int dropCount) {
        this.dropCount = dropCount;
    }

    public int getDropCoins() {
        return dropCoins;
    }

    public void setDropCoins(int dropCoins) {
        this.dropCoins = dropCoins;
    }

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(int rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public TbjGame getGame() {
        return game;
    }

    public void setGame(TbjGame game) {
        this.game = game;
    }
}
