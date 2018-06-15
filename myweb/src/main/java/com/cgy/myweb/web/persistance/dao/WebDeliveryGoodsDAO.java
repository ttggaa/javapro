package com.kariqu.zwsrv.web.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WebDeliveryGoodsDAO extends JpaRepository<DeliveryGoods, Integer> {
    @Query("select l from DeliveryGoods l where l.type=:type and l.orderId in :order_id")
    List<DeliveryGoods> findDeliveryGoodsByOrderID(@Param("type") int type, @Param("order_id") Integer[] order_id);

}
