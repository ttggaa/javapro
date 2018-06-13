package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 26/11/17.
 */
public interface PayOrderDAO extends JpaRepository<PayOrder, Integer> {

    //    @Query("select l from CoinsOrder l where l.orderSN = :orderSN")
    public PayOrder findByPaySN(/*@Param("orderSN")*/String paySN);
}

