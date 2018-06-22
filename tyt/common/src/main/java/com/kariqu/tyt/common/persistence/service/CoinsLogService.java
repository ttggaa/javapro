package com.kariqu.tyt.common.persistence.service;

import com.kariqu.tyt.common.persistence.entity.CoinsLogEntity;
import com.kariqu.tyt.common.persistence.repository.CoinsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinsLogService {

    @Autowired
    private CoinsLogRepository coinsLogRepository;

    public CoinsLogRepository getCoinsLogRepository() {
        return coinsLogRepository;
    }

    public CoinsLogEntity saveEntity(CoinsLogEntity coinsLogEntity) {
        return getCoinsLogRepository().save(coinsLogEntity);
    }
}
