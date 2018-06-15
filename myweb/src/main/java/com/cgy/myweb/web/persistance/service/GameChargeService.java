package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.GameCoinsOrder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Service
public class GameChargeService {
    @PersistenceContext
    protected EntityManager entityManager;

    public long count_search(int user_id, String date_start, String date_end) {
        String sql = "select count(1) from zww_coins_order WHERE 1=1 ";
        if (user_id > 0) {
            sql += "and user_id = ";
            sql += String.valueOf(user_id);
        }
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }

    public List<GameCoinsOrder> limit_search(int start, int count, int user_id, String date_start, String date_end) {
        String sql = "SELECT `order_id`,  `order_sn`,  `order_subject`,  `order_desc`,  `user_id`,  `coins_id`," +
                "`unit_amount`,  `unit_coins`,  `is_promotion`,  `total_amount`,  `coins`," +
                "`is_paid`,  `opt_lock`,  `updatetime`,  `createtime`,  `billing_type`,  `billing_id` FROM zww_coins_order WHERE 1=1 ";

        if (user_id > 0) {
            sql += " and user_id = ";
            sql += String.valueOf(user_id);
        }
        if (date_start != "" && date_end != "") {
            sql += " and FROM_UNIXTIME(createtime/1000,'%Y-%m-%d %H:%i:%S') BETWEEN '";
            sql += String.valueOf(date_start);
            sql += "' and '";
            sql += String.valueOf(date_end);
            sql += "' ";
        }

        sql += " order by order_id desc limit ?, ?;";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        int paramsindex = 1;
        query.setParameter(paramsindex, start);
        paramsindex += 1;
        query.setParameter(paramsindex, count);
        return GameCoinsOrder.parseRowList(query.getResultList());
    }
}
