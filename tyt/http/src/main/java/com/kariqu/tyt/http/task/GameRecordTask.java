package com.kariqu.tyt.http.task;

import com.kariqu.tyt.common.persistence.entity.GameRecordEntity;

public class GameRecordTask extends BaseTask {
    private GameRecordEntity entity;

    public GameRecordTask(GameRecordEntity gameRecordEntity) {
        super(BaseTask.TYPE_GAME_RECORD);
        this.entity = gameRecordEntity;
    }

    public GameRecordEntity getEntity() {
        return entity;
    }
}
