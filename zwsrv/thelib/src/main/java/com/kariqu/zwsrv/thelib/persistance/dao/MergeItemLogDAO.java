package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.MergeItemLog;
import com.kariqu.zwsrv.thelib.persistance.entity.UserItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MergeItemLogDAO extends JpaRepository<MergeItemLog, Integer> {

    @Query("select l from MergeItemLog l where l.userId = :userId")
    public List<MergeItemLog> findAll(@Param("userId") int userId, Pageable pageable);
}
