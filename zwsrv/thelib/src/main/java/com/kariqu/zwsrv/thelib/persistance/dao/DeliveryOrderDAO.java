package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 04/11/17.
 */
public interface DeliveryOrderDAO extends JpaRepository<DeliveryOrder, Integer> {

//    @Query("select l from DeliveryOrder l where l.userId=:userId")
//    DeliveryOrder findOne(int userId) {
//        Query query = entityManager.createQuery("select g from Entity g where g.codeUrl = :codeUrl ORDER BY g.createTime DESC");
//        query.setParameter("codeUrl", codeUrl);
//        return (Entity) query.setMaxResults(1).getSingleResult();// 仅返回一条记录
//    }

    int countByUserId(int userId);

}
