package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.TbjGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TbjGameDAO extends JpaRepository<TbjGame, Integer> {
}
