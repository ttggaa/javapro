package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.WebExpressage;
import com.kariqu.zwsrv.web.persistance.entityex.WebRoom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class WebRoomService {
    @PersistenceContext
    protected EntityManager entityManager;

    public long count(int is_online)
    {
        String sql = "select count(1) from zww_room where is_online = ?;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, is_online);

        BigInteger v = (BigInteger)query.getResultList().get(0);
        return v.longValue();
    }

    public List<WebRoom> limit(int start, int count, int is_online)
    {
        String sql = "SELECT t1.room_id, " +
                " t1.name, " +
                " t1.unlimit_times," +
                " t1.room_cost," +
                " t1.can_delivery," +
                " t1.exchange_coins," +
                " t1.reward_coins, " +
                " t1.is_online, " +
                " t1.points," +
                " t1.user_num, " +
                " t1.goods_num, " +
                " t1.is_in_unlimit, " +
                " t1.status, " +
                " t1.maint_status, " +
                " t1.device_params, " +
                " t1.sort, " +
                " t1.updatetime, " +
                " t1.createtime " +
                " FROM zww_room as t1 " +
                " WHERE is_online = ? " +
                " order by t1.createtime desc limit ?, ?;";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,is_online);
        query.setParameter(2, start);
        query.setParameter(3, count);
        return WebRoom.parseRowList(query.getResultList());
    }

    public WebRoom loadRoomInfo(int room_id)
    {
        String sql = "SELECT t1.room_id, " +
                " t1.name, " +
                " t1.unlimit_times," +
                " t1.room_cost," +
                " t1.can_delivery," +
                " t1.exchange_coins," +
                " t1.reward_coins, " +
                " t1.is_online, " +
                " t1.points," +
                " t1.user_num, " +
                " t1.goods_num, " +
                " t1.is_in_unlimit, " +
                " t1.status, " +
                " t1.maint_status, " +
                " t1.device_params, " +
                " t1.sort, " +
                " t1.updatetime, " +
                " t1.createtime " +
                " FROM zww_room as t1 " +
                " WHERE t1.room_id = ?";

        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, room_id);
        List l = query.getResultList();
        if (l.size() == 0)
            return null;
        return WebRoom.parseOneRow(l.get(0));
    }
}
