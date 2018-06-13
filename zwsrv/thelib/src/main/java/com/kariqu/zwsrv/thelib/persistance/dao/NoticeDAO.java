package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.AccountLog;
import com.kariqu.zwsrv.thelib.persistance.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 4/26/16.
 */
public interface NoticeDAO extends JpaRepository<Notice, Integer> {

    @Query("select l from Notice l where l.userId=:userId or l.userId=0")
    public List<Notice> findNotices(@Param("userId") int userId, Pageable pageable);

}
