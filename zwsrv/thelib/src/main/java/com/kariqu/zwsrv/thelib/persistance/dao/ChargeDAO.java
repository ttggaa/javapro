package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChargeDAO extends JpaRepository<Charge, Integer> {

    @Query("select l from Charge l where is_valid=1 order by order_by")
    public List<Charge> findAllCharge();

    @Query("select l from Charge l where is_valid=1 and charge_type=:type")
    public List<Charge> findByType(@Param("type") int type);
}
