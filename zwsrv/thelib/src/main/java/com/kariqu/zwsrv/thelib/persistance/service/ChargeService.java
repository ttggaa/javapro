package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.ChargeDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChargeService extends BaseService<Charge> {

    public static final int CHARGE_TYPE_FIXED = 0;             // 固定充值
    public static final int CHARGE_TYPE_FIRST_TIME = 20;       // 充值首冲-浮动的
    public static final int CHARGE_TYPE_NEW_USER_GIFT   = 30;  // 新手礼包

    // 角标
    public static final int CORNER_MARK_RECOMMEND = 1;      // 推荐
    public static final int CORNER_MARK_HOT = 2;            // 热销
    public static final int CORNER_MARK_DOUBLE = 3;         // 翻倍

    @Autowired
    private ChargeDAO cardDAO;

    @Override
    protected JpaRepository<Charge, Integer> getDao() {
        return cardDAO;
    }

    public List<Charge> findAllChargeAsList() { return cardDAO.findAllCharge(); }

    public Charge findByType(int type)
    {
        List<Charge> list = cardDAO.findByType(type);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public Map<Integer, Charge> findAllChargeAsMap()
    {
        List<Charge> list = findAllChargeAsList();
        Map<Integer, Charge> map = new HashMap<>();
        for (Charge c : list) {
            map.put(c.getChargeId(), c);
        }
        return map;
    }

}

