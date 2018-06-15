package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.WebExpressage;
import com.kariqu.zwsrv.web.persistance.entityex.WebExpressageGood;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebDeliveryOrderService  {
    @PersistenceContext
    protected EntityManager entityManager;

    public Map<Integer, String> loadWebConfig()
    {
        String sql = "select id, value from web_config;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        List rows = query.getResultList();
        Map<Integer, String> ret = new HashMap<Integer, String>();
        for (Object row : rows) {
            Object[] cells = (Object[]) row;
            int id = (int)cells[0];
            String value = (String)cells[1];

            ret.put(id, value);
        }
        return ret;
    }

    public long count(int shipping_status)
    {
        String sql = "select count(order_id) from zww_delivery_order where shipping_status = ?;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,shipping_status);

        BigInteger v = (BigInteger)query.getResultList().get(0);
        return v.longValue();
    }

    public List<WebExpressage> limit(int start, int count, int shipping_status)
    {
        String sql = "SELECT t1.order_id, " +
                " t1.shipping_name, " +
                " t2.consignee, " +
                " t2.mobile," +
                " t2.address," +
                " t1.remarks," +
                " t1.shipping_status," +
                " t1.createtime, " +
                " t1.updatetime, " +
                " t2.province," +
                " t2.city, " +
                " t2.district, " +
                " t2.user_id " +
                " FROM zww_delivery_order as t1, zww_user_address as t2 where t1.address_id = t2.address_id" +
                " and shipping_status = ? " +
                " order by t1.createtime desc limit ?, ?;";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,shipping_status);
        query.setParameter(2, start);
        query.setParameter(3, count);
        return WebExpressage.parseRowList(query.getResultList());
    }

    public long count_search(int shipping_status, int order_id, String ship_name, String rev_name, long mobile, int user_id, String date_start, String date_end) {
        String sql = "select count(order_id) from zww_delivery_order as t1, zww_user_address as t2 where t1.address_id = t2.address_id and shipping_status = ";
        sql += String.valueOf(shipping_status);
        if (order_id > 0) {
            sql += " and order_id = ";
            sql += String.valueOf(order_id);
        }
        if (ship_name != "") {
            sql += " and shipping_name LIKE ";
            sql += "'%";
            sql += String.valueOf(ship_name);
            //sql += "圆通";
            sql += "%'";
        }
        if (rev_name != "") {
            sql += " and t2.consignee LIKE ";
            sql += "'%";
            sql += String.valueOf(rev_name);
            sql += "%'";
        }
        if (mobile > 0) {
            sql += " and t2.mobile LIKE ";
            sql += "'%";
            sql += String.valueOf(mobile);
            sql += "%'";
        }
        if (user_id > 0) {
            sql += " and t1.user_id = ";
            sql += String.valueOf(user_id);
        }
        if (date_start != "" && date_end != "") {
            sql += " and FROM_UNIXTIME(t1.createtime/1000,'%Y-%m-%d %H:%i:%S') BETWEEN '";
            sql += String.valueOf(date_start);
            sql += "' and '";
            sql += String.valueOf(date_end);
            sql += "' ";
        }
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }

    public List<WebExpressage> limit_search(int start, int count, int shipping_status,int order_id, String ship_name,
                                            String rev_name, long mobile,int user_id, String date_start, String date_end) {
        String sql = "SELECT t1.order_id, " +
                " t1.shipping_name, " +
                " t2.consignee, " +
                " t2.mobile," +
                " t2.address," +
                " t1.remarks," +
                " t1.shipping_status," +
                " t1.createtime, " +
                " t1.updatetime, " +
                " t2.province," +
                " t2.city, " +
                " t2.district, " +
                " t2.user_id " +
                " FROM zww_delivery_order as t1, zww_user_address as t2 where t1.address_id = t2.address_id" +
                " and shipping_status = ? ";

        if (order_id > 0)
            sql += " and t1.order_id = ?";
        if (ship_name != "") {
            sql += " and t1.shipping_name LIKE ";
            sql += "'%";
            sql += String.valueOf(ship_name);
            //sql += "圆通";
            sql += "%'";
        }
        if (rev_name != "") {
            sql += " and t2.consignee LIKE ";
            sql += "'%";
            sql += String.valueOf(rev_name);
            sql += "%'";
        }
        if (mobile > 0) {
            sql += " and t2.mobile LIKE ";
            sql += "'%";
            sql += String.valueOf(mobile);
            sql += "%'";
        }
        if (user_id > 0) {
            sql += " and t1.user_id = ";
            sql += String.valueOf(user_id);
        }
        if (date_start != "" && date_end != "") {
            sql += " and FROM_UNIXTIME(t1.createtime/1000,'%Y-%m-%d %H:%i:%S') BETWEEN '";
            sql += String.valueOf(date_start);
            sql += "' and '";
            sql += String.valueOf(date_end);
            sql += "' ";
        }

        sql += " order by t1.createtime desc limit ?, ?;";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        int paramsindex = 1;
        query.setParameter(paramsindex, shipping_status);
        paramsindex += 1;
        if (order_id > 0) {
            query.setParameter(paramsindex, order_id);
            paramsindex += 1;
        }
        query.setParameter(paramsindex, start);
        paramsindex += 1;
        query.setParameter(paramsindex, count);
        return WebExpressage.parseRowList(query.getResultList());
    }

    public WebExpressage loadByOrderID(int order_id)
    {
        String sql =
                "SELECT t1.order_id, " +
                        " t1.shipping_name, " +
                        " t2.consignee, " +
                        " t2.mobile," +
                        " t2.address," +
                        " t1.remarks," +
                        " t1.shipping_status," +
                        " t1.createtime, " +
                        " t1.updatetime, " +
                        " t2.province," +
                        " t2.city, " +
                        " t2.district, " +
                        " t2.user_id " +
                        " FROM zww_delivery_order as t1, zww_user_address as t2 where t1.address_id = t2.address_id" +
                        " and t1.order_id = ?";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, order_id);
        List l = query.getResultList();
        if (l.size() == 0)
            return null;
        return WebExpressage.parseOneRow(l.get(0));
    }

    // 发货zww_delivery_order
    @Transactional
    public boolean orderFaHuo(int order_id, long user_id, int shipping_status, int express_status, long timestamp)
    {
        try {
            String sql_1 = "update `zww_delivery_order` set `shipping_status` = ?, `updatetime` = ? where order_id = ?;";
            javax.persistence.Query query_1 = entityManager.createNativeQuery(sql_1);
            query_1.setParameter(1, shipping_status);
            query_1.setParameter(2, timestamp);
            query_1.setParameter(3, order_id);
            query_1.executeUpdate();

            String sql_2 =
                    "update `zww_game` set `shipping_status` = ?, `updatetime` = ? where `game_id` in " +
                            " (select `idValue` from `zww_delivery_goods` where `order_id` = ?);";
            javax.persistence.Query query_2 = entityManager.createNativeQuery(sql_2);
            query_2.setParameter(1, shipping_status);
            query_2.setParameter(2, timestamp);
            query_2.setParameter(3, order_id);
            query_2.executeUpdate();

            String sql_3 =
                    "update `zww_user_item` set `express_status` = ? where `order_id` = ? and `user_id` = ? ";
            javax.persistence.Query query_3 = entityManager.createNativeQuery(sql_3);
            query_3.setParameter(1, express_status);
            query_3.setParameter(2, order_id);
            query_3.setParameter(3, user_id);
            query_3.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    //删除订单
    @Transactional
    public boolean orderDelete(int order_id)
    {
        try{
            String sql = "update `zww_delivery_order` set `shipping_status` = 99 where order_id = ?;";
            javax.persistence.Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1,order_id);
            query.executeUpdate();
            return  true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    //恢复订单
    @Transactional
    public boolean orderRecover(int order_id)
    {
        try{
            String sql = "update `zww_delivery_order` set `shipping_status` = 0 where order_id = ?;";
            javax.persistence.Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1,order_id);
            query.executeUpdate();
            return  true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    // 待处理快递物品统计
    public long goodcount()
    {
        String sql = "SELECT COUNT(1) FROM (SELECT 1 FROM `zww_delivery_order` AS t1, `zww_delivery_goods` AS t2 " +
                "WHERE t1.order_id = t2.`order_id` AND `shipping_status` = 0 GROUP BY t2.`name`)a;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger)query.getResultList().get(0);
        return v.longValue();
    }

    public List<WebExpressageGood> goodlimit(int start, int count)
    {
        String sql = "SELECT t2.name, COUNT(1) AS good_sum FROM " +
                " zww_delivery_order AS t1, zww_delivery_goods AS t2 " +
                " WHERE t1.order_id = t2.order_id " +
                " AND shipping_status = 0" +
                " GROUP BY t2.name " +
                " order by good_sum desc limit ?, ?;";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, start);
        query.setParameter(2, count);
        return WebExpressageGood.parseRowList(query.getResultList());
    }

    public long goodsum() {
        String sql = "SELECT COUNT(1) AS good_sum FROM `zww_delivery_order` AS t1, `zww_delivery_goods` AS t2 " +
                "WHERE t1.order_id = t2.`order_id` AND `shipping_status` = 0 ;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }
}
