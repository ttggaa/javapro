package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.thelib.persistance.entity.RoomSort;
import com.kariqu.zwsrv.thelib.persistance.service.RoomSortService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomSortServiceCache extends RoomSortService {

    public static int ROOM_SROT_GROUP_RANDOM  = 0; // 随机排序

    public List<RoomSort> findAllValidRoomSort()
    {
        List<RoomSort> all = roomSortDAO.findAllValidRoomSort();
        if (all != null)
            return all;
        return new ArrayList<>();
    }

    public List<RoomSort> findValidRoomSortBySortGroup(int sort_group)
    {
        List<RoomSort> all = roomSortDAO.findValidRoomSortBySortGroup(sort_group);
        if (all != null)
            return all;
        return new ArrayList<>();
    }

}
