package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.CoinsDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Coins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 26/11/17.
 */
@Service
public class CoinsService extends BaseService<Coins> {

    @Autowired
    private CoinsDAO coinsDAO;

    @Override
    protected JpaRepository<Coins, Integer> getDao() {
        return coinsDAO;
    }

    public List<Coins> findAllCoins() {
        return coinsDAO.findAllCoins();
    }

}
