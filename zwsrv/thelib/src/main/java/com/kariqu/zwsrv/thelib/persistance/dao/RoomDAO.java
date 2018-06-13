package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
public interface RoomDAO extends JpaRepository<Room, Integer> {

//    @Query("select l from Auth l where l.userId = :userId")
//    public List<Auth> findAllByUserId(@Param("userId") int userId);

    //只获取已完成的订单，包括未付款的(按照当前业务逻辑未付款的已经不存在了)
    @Query("select l from Room l where l.hasParent = 0 and l.isOnline=1 and l.valid=1")
    public List<Room> findOnlineRooms(Pageable pageable);//发布的:完成的订单和关闭的订单

    @Query("select l from Room l where l.hasParent = 0 and l.isOnline=1 and l.valid=1")
    public List<Room> findAllOnlineRooms();

}
