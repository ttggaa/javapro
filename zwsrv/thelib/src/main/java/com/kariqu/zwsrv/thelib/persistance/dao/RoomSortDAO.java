package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.RoomSort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomSortDAO extends JpaRepository<RoomSort, Integer> {

    @Query("select l from RoomSort l where l.isValid = 1 order by l.sortValue desc")
    public List<RoomSort> findAllValidRoomSort();

    @Query("select l from RoomSort l where l.isValid = 1 and l.sortGroup = :sort_group order by l.sortValue desc")
    public List<RoomSort> findValidRoomSortBySortGroup(@Param("sort_group") int sort_group);

}
