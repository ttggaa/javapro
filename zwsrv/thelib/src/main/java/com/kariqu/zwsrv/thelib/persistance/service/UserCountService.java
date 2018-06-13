package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.UserCountDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.UserCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 18/04/17.
 */
@Service
public class UserCountService extends BaseService<UserCount> {

    @Autowired
    private UserCountDAO userCountDAO;

    @Override
    protected JpaRepository<UserCount, Integer> getDao() {
        return userCountDAO;
    }


}
