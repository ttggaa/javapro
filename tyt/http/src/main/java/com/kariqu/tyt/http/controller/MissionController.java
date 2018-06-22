package com.kariqu.tyt.http.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.kariqu.tyt.common.persistence.entity.CoinsLogEntity;
import com.kariqu.tyt.common.persistence.entity.MissionEntity;
import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.common.persistence.entity.UserMissionEntity;
import com.kariqu.tyt.common.persistence.redis.BaseRedis;
import com.kariqu.tyt.http.model.UserMissionModel;
import com.kariqu.tyt.http.pkg.ReqGetMission;
import com.kariqu.tyt.http.pkg.ReqMissionReward;
import com.kariqu.tyt.http.pkg.ResponseData;
import com.kariqu.tyt.http.pkg.ResponseError;
import com.kariqu.tyt.http.redis.Redis;
import com.kariqu.tyt.http.service.MissionCachedService;
import com.kariqu.tyt.http.service.UserCachedService;
import com.kariqu.tyt.http.service.UserMissionCachedService;
import com.kariqu.tyt.http.task.CoinsLogTask;
import com.kariqu.tyt.http.task.TaskManager;
import com.kariqu.tyt.http.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("tyt/mission/v1")
public class MissionController {
    private static final Logger logger = LoggerFactory.getLogger(MissionController.class);

    @Autowired
    private MissionCachedService missionCachedService;

    @Autowired
    private UserMissionCachedService userMissionCachedService;

    @Autowired
    private UserCachedService userCachedService;

    @PostMapping(value = "/get_mission")
    public ResponseData get_mission(@RequestBody(required = true) String requestBody) {
        try {
            ReqGetMission req = Utility.parseJsonToObject(requestBody, ReqGetMission.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            int userId = req.getUserId();
            List<UserMissionEntity> userMissionEntityList = userMissionCachedService.findUserMission(userId);
            if (userMissionEntityList == null) {
                return ResponseError.UnknownError;
            }

            List<UserMissionModel> data = new ArrayList<>();
            for (UserMissionEntity entity : userMissionEntityList) {
                UserMissionModel model = createUserMissionModel(entity);
                if (model == null) {
                    logger.warn("can't find mission. id: {} uid: {}", entity.getMissionId(), userId);
                    return ResponseError.UnknownError;
                }
                data.add(model);
            }
            return new ResponseData().put("list", data);
        } catch (Exception e) {
            logger.warn("exception. {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/mission_reward")
    public ResponseData mission_reward(@RequestBody(required = true) String requestBody) {
        try {
            ReqMissionReward req = Utility.parseJsonToObject(requestBody, ReqMissionReward.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            int userId = req.getUserId();
            List<UserMissionEntity> userMissionEntityList = userMissionCachedService.findUserMission(userId);
            if (userMissionEntityList == null) {
                logger.warn("can't find user mission. uid: {}", userId);
                return ResponseError.UnknownError;
            }

            UserMissionEntity userMissionEntity = null;
            for (UserMissionEntity entity : userMissionEntityList) {
                if (entity.getMissionId() == req.getMissionId()) {
                    userMissionEntity = entity;
                    break;
                }
            }
            if (userMissionEntity == null) {
                logger.warn("user mission is null. uid: {} mission_id: {}", req.getUserId(), req.getMissionId());
                return ResponseError.UnknownError;
            }

            if (userMissionEntity.getState() != UserMissionEntity.STATE_UNREWARD) {
                return ResponseError.MissionCannotReward;
            }

            userMissionEntity.setState(UserMissionEntity.STATE_REWARDED);
            MissionEntity missionEntity = missionCachedService.findMission(userMissionEntity.getMissionId());
            if (missionEntity == null) {
                logger.warn("can't find mission id: {}", userMissionEntity.getMissionId());
                return ResponseError.UnknownError;
            }

            UserEntity userEntity = userCachedService.findUserByUserId(req.getUserId());
            if (userEntity == null) {
                logger.warn("can't find user. userid: {}", req.getUserId());
                return ResponseError.UnknownError;
            }

            userEntity.changeCoins(missionEntity.getRewardCoin());
            userMissionEntity.setState(UserMissionEntity.STATE_REWARDED);
            userMissionCachedService.asyncSaveUserMissions(req.getUserId(), userMissionEntityList);
            userCachedService.asyncSaveEntity(userEntity);

            {
                CoinsLogEntity coinsLogEntity = new CoinsLogEntity();
                coinsLogEntity.setType(CoinsLogEntity.TYPE_MISSION_REWARD);
                coinsLogEntity.setUserId(userId);
                coinsLogEntity.setCoinsChange(missionEntity.getRewardCoin());
                coinsLogEntity.setCoinsReamin(userEntity.getCoins());
                TaskManager.getInstace().put(new CoinsLogTask(coinsLogEntity));
            }

            List<UserMissionModel> data = new ArrayList<>();
            for (UserMissionEntity entity : userMissionEntityList) {
                UserMissionModel model = createUserMissionModel(entity);
                if (model == null) {
                    return ResponseError.UnknownError;
                }
                data.add(model);
            }
            return new ResponseData().put("list", data);
        } catch (Exception e) {
            logger.warn("exception. {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    private UserMissionModel createUserMissionModel(UserMissionEntity userMissionEntity) {
        MissionEntity missionEntity = missionCachedService.findMission(userMissionEntity.getMissionId());
        if (missionEntity == null)
            return null;
        return new UserMissionModel(userMissionEntity, missionEntity);
    }
}
