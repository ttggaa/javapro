package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.persistance.entity.Game;


/**
 * 增加寄存剩余时间属性
 */

public class GameStorageSecondsInfo extends GameInfo {
    private long storageExpiredTime; // 寄存剩余时间

    public GameStorageSecondsInfo(Game game, long goodsMaxStorageTime, long currentMilliseconds) {
        super(game);

        long temp = (getEndTime() + goodsMaxStorageTime) - currentMilliseconds;
        if (temp < 0)
            temp = 0;
        this.storageExpiredTime = temp;
    }

    public long getStorageExpiredTime() {
        return storageExpiredTime;
    }

    public void setStorageExpiredTime(long storageExpiredTime) {
        this.storageExpiredTime = storageExpiredTime;
    }
}
