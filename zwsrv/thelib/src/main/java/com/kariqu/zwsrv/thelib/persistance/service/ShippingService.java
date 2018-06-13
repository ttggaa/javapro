package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.ShippingDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
@Service
public class ShippingService extends BaseService<Shipping> {

    @Autowired
    private ShippingDAO shippingDAO;

    @Override
    protected JpaRepository<Shipping, Integer> getDao() {
        return shippingDAO;
    }

    public List<Shipping> findAllShippings() {
        return shippingDAO.findAllShippings();
    }

}

