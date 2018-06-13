package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.TbjGameDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.TbjGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TbjGameService extends BaseService<TbjGame> {

    @Autowired
    protected TbjGameDAO tbjGameDAO;

    @Override
    protected JpaRepository<TbjGame, Integer> getDao() {
        return tbjGameDAO;
    }
}
