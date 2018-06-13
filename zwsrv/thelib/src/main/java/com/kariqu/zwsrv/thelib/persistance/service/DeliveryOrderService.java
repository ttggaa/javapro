package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.DeliveryOrderDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
@Service
public class DeliveryOrderService extends BaseService<DeliveryOrder> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DeliveryOrderDAO deliveryOrderDAO;

    @Override
    protected JpaRepository<DeliveryOrder, Integer> getDao() {
        return deliveryOrderDAO;
    }

    public DeliveryOrder findOneByUserId(int userId) {
        javax.persistence.Query query = entityManager.createQuery("select l from DeliveryOrder l where l.userId = :userId ORDER BY l.createTime DESC");
        query.setParameter("userId", Integer.valueOf(userId));
        List<Object> list = query.setMaxResults(1).getResultList();
        if (list!=null&&list.size()>0) {
            return  (DeliveryOrder)list.get(0);
        }
        return null;
    }

    public int countByUserId(int userId) {
        return deliveryOrderDAO.countByUserId(userId);
    }

}
