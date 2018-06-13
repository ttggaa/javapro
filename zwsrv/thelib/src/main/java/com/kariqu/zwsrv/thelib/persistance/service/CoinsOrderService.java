package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.CoinsOrderDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.CoinsOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 26/11/17.
 */
@Service
public class CoinsOrderService extends BaseService<CoinsOrder> {

    @Autowired
    private CoinsOrderDAO coinsOrderDAO;

    @Override
    protected JpaRepository<CoinsOrder, Integer> getDao() {
        return coinsOrderDAO;
    }


    //    @Query("select l from CoinsOrder l where l.orderSN = :orderSN")
    public CoinsOrder findByOrderSN(/*@Param("orderSN")*/String orderSN) {
        return coinsOrderDAO.findByOrderSN(orderSN);
    }

}
