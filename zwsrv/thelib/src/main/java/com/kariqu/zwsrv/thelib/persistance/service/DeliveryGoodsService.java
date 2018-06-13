package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.DeliveryGoodsDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
@Service
public class DeliveryGoodsService extends BaseService<DeliveryGoods> {

    @Autowired
    private DeliveryGoodsDAO deliveryGoodsDAO;

    @Override
    protected JpaRepository<DeliveryGoods, Integer> getDao() {
        return deliveryGoodsDAO;
    }

    public List<DeliveryGoods> findDeliveryGoods(int type, List<Integer> idvalues) {
        return deliveryGoodsDAO.findDeliveryGoods(type,idvalues.toArray(new Integer[idvalues.size()]));
    }

}
