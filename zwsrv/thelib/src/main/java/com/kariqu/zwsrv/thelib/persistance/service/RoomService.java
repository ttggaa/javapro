package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.RoomDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
@Service
public class RoomService extends BaseService<Room> {

    @Autowired
    private RoomDAO machineDAO;

    @Override
    protected JpaRepository<Room, Integer> getDao() {
        return machineDAO;
    }

    public List<Room> findOnlineRooms(int page, int size) {
        return machineDAO.findOnlineRooms(new PageRequest(page, size, Sort.Direction.DESC, "sort"));
    }

    public List<Room> findAllOnlineRooms() {
        List<Room> all = machineDAO.findAllOnlineRooms();
        if (all != null)
            return all;
        return new ArrayList<>();
    }

}
