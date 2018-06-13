package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.RoomSortDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.RoomSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomSortService extends BaseService<RoomSort> {

    @Autowired
    protected RoomSortDAO roomSortDAO;

    @Override
    protected JpaRepository<RoomSort, Integer> getDao() {
        return roomSortDAO;
    }
}
