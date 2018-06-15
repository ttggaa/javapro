package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.WebProduct;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class WebProductService {
    @PersistenceContext
    protected EntityManager entityManager;

    public List<WebProduct> getGameData() {
        String sql = "CALL `sp_gameinfo`(0);";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return WebProduct.parseRowList(query.getResultList());
    }

    public List<String> getDays(){
        String sql = "SELECT DISTINCT DATE_FORMAT( FROM_UNIXTIME(updatetime/1000,'%Y-%m-%d'), '%Y-%m-%d') AS dd" +
                " FROM " +
                " `zww_game`" +
                " WHERE DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= FROM_UNIXTIME(updatetime/1000,'%Y-%m-%d')" +
                " ORDER BY dd";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return WebProduct.getDays(query.getResultList());
    }
}
