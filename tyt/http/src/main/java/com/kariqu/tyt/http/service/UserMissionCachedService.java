package com.kariqu.tyt.http.service;

import com.google.gson.Gson;
import com.kariqu.tyt.common.persistence.entity.UserMissionEntity;
import com.kariqu.tyt.common.persistence.service.UserMissionService;
import com.kariqu.tyt.http.redis.Redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class UserMissionCachedService extends UserMissionService {
    private static final Logger logger = LoggerFactory.getLogger(UserMissionCachedService.class);

    @Autowired
    private Redis redis;

    @Autowired
    private EntityManager entityManager;

    public List<UserMissionEntity> findUserMission(int userId) {
        List<UserMissionEntity> missionEntities = redis.getUserMission(userId);
        if (missionEntities == null || missionEntities.isEmpty()) {
            missionEntities = findAllUserMissionByUserId(userId);
            if (missionEntities != null) {
                redis.saveUserMission(userId, missionEntities);
            }
        }
        return missionEntities;
    }

    public List<UserMissionEntity> asyncSaveUserMissions(int userId, List<UserMissionEntity> userMissionEntityList) {
        redis.saveUserMission(userId, userMissionEntityList);
        redis.saveUserMissionTag(userId);
        return userMissionEntityList;
    }

    @Transactional
    public List<UserMissionEntity> syncSaveUserMissions(int userId, List<UserMissionEntity> userMissionEntityList) {
        if (userMissionEntityList.isEmpty())
            return userMissionEntityList;

        StringBuilder sb = new StringBuilder();

        String sql = "replace into `tyt_user_mission` " +
                " (`user_id`, `index`, `mission_id`, `current_goal`, `state`, `opt_lock`, `updatetime`, `createtime`) ";
        sb.append(sql);
        sb.append(" VALUES ");
        for (int i = 0; i != userMissionEntityList.size(); ++i) {
            if (i != 0)
                sb.append(',');
            sb.append("(?,?,?,?, ?,?,?,?)");
        }
        sql = sb.toString();
        Query query = entityManager.createNativeQuery(sql);
        int cnt = 0;
        for (UserMissionEntity e : userMissionEntityList) {
            query.setParameter(++cnt, e.getUserId());
            query.setParameter(++cnt, e.getIndex());
            query.setParameter(++cnt, e.getMissionId());
            query.setParameter(++cnt, e.getCurrentGoal());
            query.setParameter(++cnt, e.getState());
            query.setParameter(++cnt, 0);       // opt_lock
            query.setParameter(++cnt, new Date());    // updatetime
            query.setParameter(++cnt, new Date());    // createtime
        }
        query.executeUpdate();
        return userMissionEntityList;
    }

    private List<UserMissionEntity> findAllUserMissionByUserId(int userId) {
        String sql =
                " SELECT `user_id`, `index`, `mission_id`, `current_goal`, `state`, "
                + "`opt_lock`, `updatetime`, `createtime` FROM `tyt_user_mission` WHERE `user_id` = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);
        List resultList = query.getResultList();

        List<UserMissionEntity> userMissionEntityList = new ArrayList<>();
        for (Object obj : resultList) {
            UserMissionEntity entity = parseUserMissionEntityFromObject(obj);
            //Gson gons = new Gson();
            userMissionEntityList.add(entity);
        }
        return userMissionEntityList;
    }

    private static UserMissionEntity parseUserMissionEntityFromObject(Object obj) {
        if (obj == null)
            return null;
        UserMissionEntity entity =new UserMissionEntity();
        Object[] row = (Object[])obj;
        int n = 0;
        entity.setUserId((int)row[n++]);
        entity.setIndex((int)row[n++]);
        entity.setMissionId((int)row[n++]);
        entity.setCurrentGoal((int)row[n++]);
        entity.setState((int)row[n++]);
        entity.setOptLock(((BigInteger)row[n++]).longValue());

        entity.setUpdatetime((Date)row[n++]);
        entity.setCreatetime((Date)row[n++]);
        return entity;
    }

}
