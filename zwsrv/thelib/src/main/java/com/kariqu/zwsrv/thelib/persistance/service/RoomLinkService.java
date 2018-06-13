package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.RoomLinkDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.RoomLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 09/12/17.
 */
@Service
public class RoomLinkService extends BaseService<RoomLink> {

    @Autowired
    private RoomLinkDAO roomLinkDAO;

    @Override
    protected JpaRepository<RoomLink, Integer> getDao() {
        return roomLinkDAO;
    }

}
