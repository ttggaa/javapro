package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.AccountLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 14/02/17.
 */
public interface AccountLogDAO extends JpaRepository<AccountLog, Integer> {

    @Query("select l from AccountLog l where l.userId=:userId and l.coinsChangedType in :coinsChangedTypes")
    public List<AccountLog> findAccountCoinLogs(@Param("userId") int userId, @Param("coinsChangedTypes") Integer[] coinsChangedTypes, Pageable pageable);


    @Query("select l from AccountLog l where l.userId=:userId and l.pointsChangedType in :pointsChangedTypes")
    public List<AccountLog> findAccountPointLogs(@Param("userId") int userId, @Param("pointsChangedTypes") Integer[] pointsChangedTypes, Pageable pageable);


}

