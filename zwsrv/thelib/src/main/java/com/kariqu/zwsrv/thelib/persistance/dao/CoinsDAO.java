package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Coins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 26/11/17.
 */
public interface CoinsDAO extends JpaRepository<Coins, Integer> {

    @Query("select l from Coins l where l.valid=1 order by sort desc")
    public List<Coins> findAllCoins();

}
