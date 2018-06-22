package com.kariqu.tyt.http.task;

import com.kariqu.tyt.common.persistence.entity.CoinsLogEntity;

public class CoinsLogTask extends BaseTask {
    private CoinsLogEntity entity;

    public CoinsLogTask(CoinsLogEntity coinsLogEntity) {
        super(BaseTask.TYPE_COINS_LOG);
        this.entity = coinsLogEntity;
    }

    public CoinsLogEntity getEntity() {
        return entity;
    }
}
