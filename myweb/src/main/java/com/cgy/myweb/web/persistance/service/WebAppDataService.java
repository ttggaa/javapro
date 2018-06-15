package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.WebAppData;
import com.kariqu.zwsrv.web.persistance.entityex.WebProduct;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Service
public class WebAppDataService {
    @PersistenceContext
    protected EntityManager entityManager;

    public long count(String app_channel) {
        String sql =
                "SELECT count(1) FROM ( " +
                        "SELECT dd, SUM(register) AS register, SUM(activety) AS activety, SUM(charge) AS charge " +
                        "FROM " +
                        "( " +
                        " " +
                        "SELECT FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') AS dd,  COUNT(1) AS register, 0 AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_users` " +
                        "WHERE " +
                        "app_channel = ? " +
                        "AND FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d')  > '2018-03-16'" +
                        "AND FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d')  < '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') " +
                        "union all "+
                        "SELECT FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') AS dd,  COUNT(1) AS register, 0 AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_users` " +
                        "WHERE " +
                        "reg_app_channel = ? " +
                        "AND FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d')  >= '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') " +
                        " " +
                        "UNION ALL " +
                        " " +
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd,  0 AS register, COUNT(DISTINCT player_id) AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_game` a, `zww_users` b " +
                        "WHERE  " +
                        "a.player_id = b.user_id " +
                        "AND app_channel = ? " +
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  > '2018-03-16'" +
                        "AND FROM_UNIXTIME(b.`createtime`/1000,'%Y-%m-%d')  < '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        "union all "+
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd,  0 AS register, COUNT(DISTINCT player_id) AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_game` a, `zww_users` b " +
                        "WHERE  " +
                        "a.player_id = b.user_id " +
                        "AND reg_app_channel = ?  " +
                        "AND FROM_UNIXTIME(b.`createtime`/1000,'%Y-%m-%d')  >= '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        " " +
                        "UNION ALL " +
                        " " +
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd, 0 AS register, 0 AS activety, SUM(c.total_amount)/100 AS charge " +
                        "FROM   " +
                        "`zww_coins_order` a, `zww_users` b  , `zww_pay_order` c " +
                        "WHERE " +
                        "a.user_id = b.user_id " +
                        "and c.order_id = a.order_id " +
                        "AND c.is_paid = 1 " +
                        "AND channel = ? " +
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  > '2018-04-24' " +
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        "union all "+
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd, 0 AS register, 0 AS activety, SUM(total_amount)/100 AS charge " +
                        "FROM " +
                        "`zww_coins_order` a, `zww_users` b " +
                        "WHERE " +
                        "a.user_id = b.user_id " +
                        "AND is_paid = 1 " +
                        "AND app_channel = ? " +
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  > '2018-03-16' "+
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  <= '2018-04-24' "+
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        " " +
                        ")a " +
                        "GROUP BY dd " +
                        ")a " +
                        " ;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, app_channel);
        query.setParameter(2, app_channel);
        query.setParameter(3, app_channel);
        query.setParameter(4, app_channel);
        query.setParameter(5, app_channel);
        query.setParameter(6, app_channel);

        BigInteger value = (BigInteger) query.getResultList().get(0);
        return value.longValue();
    }

    public List<WebAppData> limit(int start, int count, String app_channel) {
        String sql =
                "SELECT ? AS app_channel, dd, SUM(register) AS register, SUM(activety) AS activety, SUM(charge) AS charge " +
                        "FROM " +
                        "( " +
                        " " +
                        "SELECT FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') AS dd,  COUNT(1) AS register, 0 AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_users` " +
                        "WHERE " +
                        "app_channel = ? " +
                        "AND FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d')  > '2018-03-16'" +
                        "AND FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d')  < '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') " +
                        "union all "+
                        "SELECT FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') AS dd,  COUNT(1) AS register, 0 AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_users` " +
                        "WHERE " +
                        "reg_app_channel = ? " +
                        "AND FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d')  >= '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(`createtime`/1000,'%Y-%m-%d') " +
                        " " +
                        "UNION ALL " +
                        " " +
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd,  0 AS register, COUNT(DISTINCT player_id) AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_game` a, `zww_users` b " +
                        "WHERE  " +
                        "a.player_id = b.user_id " +
                        "AND app_channel = ? " +
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  > '2018-03-16'" +
                        "AND FROM_UNIXTIME(b.`createtime`/1000,'%Y-%m-%d')  < '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        "union all "+
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd,  0 AS register, COUNT(DISTINCT player_id) AS activety, 0 AS charge " +
                        "FROM " +
                        "`zww_game` a, `zww_users` b " +
                        "WHERE  " +
                        "a.player_id = b.user_id " +
                        "AND reg_app_channel = ?  " +
                        "AND FROM_UNIXTIME(b.`createtime`/1000,'%Y-%m-%d')  >= '2018-04-26'" +
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        " " +
                        "UNION ALL " +
                        " " +
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd, 0 AS register, 0 AS activety, SUM(c.total_amount)/100 AS charge " +
                        "FROM   " +
                        "`zww_coins_order` a, `zww_users` b  , `zww_pay_order` c " +
                        "WHERE " +
                        "a.user_id = b.user_id " +
                        "and c.order_id = a.order_id " +
                        "AND c.is_paid = 1 " +
                        "AND channel = ? " +
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  > '2018-04-24' " +
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        "union all "+
                        "SELECT FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') AS dd, 0 AS register, 0 AS activety, SUM(total_amount)/100 AS charge " +
                        "FROM " +
                        "`zww_coins_order` a, `zww_users` b " +
                        "WHERE " +
                        "a.user_id = b.user_id " +
                        "AND is_paid = 1 " +
                        "AND app_channel = ? " +
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  > '2018-03-16' "+
                        "AND FROM_UNIXTIME(a.`createtime`/1000,'%Y-%m-%d')  <= '2018-04-24' "+
                        "GROUP BY FROM_UNIXTIME(a.createtime/1000,'%Y-%m-%d') " +
                        " " +
                        ")a " +
                        "GROUP BY dd " +
                        "ORDER BY dd desc limit ?, ?" +
                        " ;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, app_channel);
        query.setParameter(2, app_channel);
        query.setParameter(3, app_channel);
        query.setParameter(4, app_channel);
        query.setParameter(5, app_channel);
        query.setParameter(6, app_channel);
        query.setParameter(7, app_channel);
        query.setParameter(8, start);
        query.setParameter(9, count);

        return WebAppData.parseRowList(query.getResultList());
    }

    public List<String> getChannelList() {
        String sql = "SELECT DISTINCT app_channel FROM `zww_users` ;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return WebAppData.channelList(query.getResultList());
    }
}
