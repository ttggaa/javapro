package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Coins;
import com.kariqu.zwsrv.thelib.persistance.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
public interface ShippingDAO extends JpaRepository<Shipping, Integer> {

    @Query("select l from Shipping l where l.valid=1 order by sort desc")
    public List<Shipping> findAllShippings();

}
