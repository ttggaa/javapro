package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.thelib.base.service.BaseService;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;
import com.kariqu.zwsrv.web.persistance.dao.WebDeliveryGoodsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Service
public class WebDeliveryGoodsService extends BaseService<DeliveryGoods> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected WebDeliveryGoodsDAO deliveryGoodsDAO;

    @Override
    protected JpaRepository<DeliveryGoods, Integer> getDao() {
        return deliveryGoodsDAO;
    }

    public List<DeliveryGoods> findDeliveryGoodsByOrderID(int type, List<Integer> order_id) {
        return deliveryGoodsDAO.findDeliveryGoodsByOrderID (type, order_id.toArray(new Integer[order_id.size()]));
    }

    public String getContactGoodName(int order_id){
        String sql = "SELECT GROUP_CONCAT(NAME) as nickname FROM zww_delivery_goods"+
                     " WHERE order_id = ? GROUP BY order_id ;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,order_id);
        return (String)query.getResultList().get(0);
    }
}
