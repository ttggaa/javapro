package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.UserAddressDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
@Service
public class UserAddressService extends BaseService<UserAddress> {

    @Autowired
    private UserAddressDAO userAddressDAO;

    @Override
    protected JpaRepository<UserAddress, Integer> getDao() {
        return userAddressDAO;
    }

    public List<UserAddress> findAddressByUserId(int userId) {
        return userAddressDAO.findAddressByUserId(userId);
    }

    public int countByUserId(int userId) {
        return userAddressDAO.countByUserId(userId);
    }

}

