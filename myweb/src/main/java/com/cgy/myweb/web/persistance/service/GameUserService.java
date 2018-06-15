package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.GameUser;
import com.kariqu.zwsrv.web.persistance.entityex.GameUserLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class GameUserService {
    @PersistenceContext
    protected EntityManager entityManager;


    /**
     * info
     **/

    public long count() {
        String sql = "select count(1) from zww_account;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }

    public List<GameUser> limit(int start, int count) {
        String sql = "select `user_id`,  `coins`,  `available_coins`,  `money`,  `available_money`,  `points`, " +
                " `available_points`,  `new_user_gift`,  `gift_datetime`,  `charge_first_time`,  `charge_ft_datetime`" +
                " from zww_account" +
                " order by user_id limit ?, ?;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, start);
        query.setParameter(2, count);
        return GameUser.parseRowList(query.getResultList());
    }

    public long count_search(int user_id, String date_start, String date_end) {
        String sql = "select count(1) from zww_account WHERE 1=1 ";
        if (user_id > 0) {
            sql += "and user_id = ";
            sql += String.valueOf(user_id);
        }
        if (date_start != "" && date_end != "") {
            sql += " and FROM_UNIXTIME(createtime/1000,'%Y-%m-%d %H:%i:%S') BETWEEN '";
            sql += String.valueOf(date_start);
            sql += "' and '";
            sql += String.valueOf(date_end);
            sql += "' ";
        }
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }

    public List<GameUser> limit_search(int start, int count, int user_id, String date_start, String date_end) {
        String sql = "SELECT `user_id`,  `coins`,  `available_coins`,  `money`,  `available_money`,  `points`,  " +
                "`available_points`,  `new_user_gift`,  `gift_datetime`,  `charge_first_time`,  `charge_ft_datetime` " +
                " FROM zww_account WHERE 1=1 ";

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

        sql += " order by user_id limit ?, ?;";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        int paramsindex = 1;
        query.setParameter(paramsindex, start);
        paramsindex += 1;
        query.setParameter(paramsindex, count);
        return GameUser.parseRowList(query.getResultList());
    }


    /**
     * info log
     **/

    public long countlog_search(int user_id) {
        String sql = "select count(1) from zww_account_log WHERE 1=1 ";
        if (user_id > 0) {
            sql += "and user_id = ";
            sql += String.valueOf(user_id);
        }
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }

    public List<GameUserLog> limitlog_search(int start, int count, int user_id) {
        String sql = "SELECT `log_id`,`user_id`,`coins`,`available_coins`,`coins_changed_type`,`money`," +
                "`available_money`,`money_changed_type`,`points`,`available_points`,`points_changed_type`," +
                "`fee`,`data`,`timestamp`,`extra_data` FROM zww_account_log WHERE 1=1 ";

        if (user_id > 0) {
            sql += " and user_id = ";
            sql += String.valueOf(user_id);
        }

        sql += " order by log_id desc limit ?, ?;";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        int paramsindex = 1;
        query.setParameter(paramsindex, start);
        paramsindex += 1;
        query.setParameter(paramsindex, count);
        return GameUserLog.parseRowList(query.getResultList());
    }

    public List<GameUserLog> findInfoLogDetail(int user_id)
    {
        String sql = "SELECT `log_id`,`user_id`,`coins`,`available_coins`,`coins_changed_type`,`money`," +
                "`available_money`,`money_changed_type`,`points`,`available_points`,`points_changed_type`," +
                "`fee`,`data`,`extra_data` FROM zww_account_log WHERE 1=1 ";

        if (user_id > 0) {
            sql += " and user_id = ";
            sql += String.valueOf(user_id);
        }
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return GameUserLog.parseRowList(query.getResultList());
    }
}
