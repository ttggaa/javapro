package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.UserAddress;
import com.kariqu.zwsrv.thelib.persistance.entity.UserCard;
import com.kariqu.zwsrv.thelib.persistance.entity.UserSignin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserCardDAO extends JpaRepository<UserCard, Integer> {

    // 能领取的卡片
    @Query("select l from UserCard l where l.userId=:userId " +
            " and :tnow < l.expiredDatetime " +
            " and l.cardType = :cardType")
    public List<UserCard> findAllValidCardByType(@Param("userId") int userId, @Param("tnow") Date tnow,  @Param("cardType") int cardType);

    // 当前未过期的卡片
    @Query("select l from UserCard l where l.userId=:userId " +
            " and :tnow < l.expiredDatetime")
    public List<UserCard> findAllValidCard(@Param("userId") int userId, @Param("tnow") Date tnow);

}
