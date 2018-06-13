package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.RoomTypeDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomTypeService extends BaseService<RoomType> {

    @Autowired
    protected RoomTypeDAO roomTypeDAO;

    @Override
    protected JpaRepository<RoomType, Integer> getDao() {
        return roomTypeDAO;
    }
}
