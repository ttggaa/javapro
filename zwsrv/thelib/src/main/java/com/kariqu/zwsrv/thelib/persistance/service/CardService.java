package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.CardDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService extends BaseService<Card> {

    public static final int CARD_TYPE_WEEKLY = 1;       // 周卡
    public static final int CARD_TYPE_MONTHLY = 2;      // 月卡

    @Autowired
    private CardDAO cardDAO;

    @Override
    protected JpaRepository<Card, Integer> getDao() {
        return cardDAO;
    }

    public Map<Integer, Card> findAllAsMap()
    {
        List<Card> list = cardDAO.findAllCard();
        Map<Integer, Card> map = new HashMap<>();
        for (Card c : list) {
            map.put(c.getCardType(), c);
        }
        return map;
    }

    public boolean isValidCardType(int type)
    {
        return type == CARD_TYPE_MONTHLY || type == CARD_TYPE_WEEKLY;
    }
}
