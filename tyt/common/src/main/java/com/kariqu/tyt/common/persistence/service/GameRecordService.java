package com.kariqu.tyt.common.persistence.service;

import com.kariqu.tyt.common.persistence.entity.GameRecordEntity;
import com.kariqu.tyt.common.persistence.repository.GameRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRecordService {

    @Autowired
    protected GameRecordRepository gameRecordRepository;

    public void saveEntity(GameRecordEntity gameRecordEntity) {
        gameRecordRepository.save(gameRecordEntity);
    }

}
