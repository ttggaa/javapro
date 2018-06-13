package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Card;
import com.kariqu.zwsrv.thelib.persistance.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardDAO extends JpaRepository<Card, Integer> {

    @Query("select l from Card l where is_valid=1")
    public List<Card> findAllCard();
}
