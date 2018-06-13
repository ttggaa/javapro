package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.CoinsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 26/11/17.
 */
public interface CoinsOrderDAO extends JpaRepository<CoinsOrder, Integer> {

    //    @Query("select l from CoinsOrder l where l.orderSN = :orderSN")
    public CoinsOrder findByOrderSN(/*@Param("orderSN")*/String orderSN);

}
