package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.thelib.persistance.entity.RoomType;
import com.kariqu.zwsrv.thelib.persistance.service.RoomTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomTypeServiceCache extends RoomTypeService {

    public List<RoomType> findAllValidRoomType()
    {
        List<RoomType> list = roomTypeDAO.findAllValidRoomType();
        if (list != null)
            return list;
        return new ArrayList<>();
    }
}
