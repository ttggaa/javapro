package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.UserCardDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.UserCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserCardService extends BaseService<UserCard> {

    public static final long CARD_REWARD_SECONDS = 24 * 3600;

    @Autowired
    protected UserCardDAO userCardDAO;

    @Override
    protected JpaRepository<UserCard, Integer> getDao() {
        return userCardDAO;
    }

    public List<UserCard> findValidCard(int userId, Date tnow)
    {
        return userCardDAO.findAllValidCard(userId, tnow);
    }

    public List<UserCard> findAllCanRewardCardByType(int userId, int card_type, Date tnow, int intervalSeconds)
    {
        List<UserCard> result = userCardDAO.findAllValidCardByType(userId, tnow, card_type);
        List<UserCard> can_reward = new ArrayList<>();
        if (result != null) {
            for (UserCard c : result) {
                if (c.getRewardDatetime().getTime() + (intervalSeconds * 1000) < tnow.getTime()) {
                    can_reward.add(c);
                }
            }
        }
        return can_reward;
    }
}
