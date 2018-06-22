package com.kariqu.tyt.common.persistence.service;

import com.kariqu.tyt.common.persistence.entity.UserMissionEntity;
import com.kariqu.tyt.common.persistence.repository.UserMissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMissionService {

    @Autowired
    protected UserMissionRepository userMissionRepository;

    public UserMissionRepository getUserMissionRepository() {
        return userMissionRepository;
    }

    /*
    public List<UserMissionEntity> findUserMission(int userId) {
        List<UserMissionEntity> list = getUserMissionRepository().findAllByUserId(userId);
        int index = 0;
        int uid = 0;
        for (UserMissionEntity entity : list) {
            uid = entity.getUserId();
            if (entity.getIndex() == index) {
                logger.warn("same index. uid: {} index: {}", uid, index);
            } else {
                index = entity.getIndex();
            }
        }
        return list;
    }
    */
}
