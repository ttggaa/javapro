package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.UserItemDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.UserItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserItemService extends BaseService<UserItem>  {

    @Autowired
    protected UserItemDAO userItemDAO;

    @Override
    protected JpaRepository<UserItem, Integer> getDao() { return userItemDAO; }
}
