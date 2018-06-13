package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.PlatDict;
import com.kariqu.zwsrv.thelib.persistance.entity.RaiseProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaiseProblemDAO extends JpaRepository<RaiseProblem, Integer> {
}
