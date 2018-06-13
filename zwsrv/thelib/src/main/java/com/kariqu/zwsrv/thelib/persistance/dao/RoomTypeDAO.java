package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomTypeDAO extends JpaRepository<RoomType, Integer> {

    @Query("select l from RoomType l where l.isValid = 1 order by l.sort desc")
    public List<RoomType> findAllValidRoomType();

}
