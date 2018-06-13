package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
public interface DeliveryGoodsDAO extends JpaRepository<DeliveryGoods, Integer> {

    @Query("select l from DeliveryGoods l where l.type=:type and l.idvalue in :idvalues")
    List<DeliveryGoods> findDeliveryGoods(@Param("type") int type, @Param("idvalues") Integer[] idvalues);

}
