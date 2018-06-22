package com.kariqu.tyt.http.controller;


import com.kariqu.tyt.common.persistence.entity.*;
import com.kariqu.tyt.common.persistence.service.UserService;
import com.kariqu.tyt.http.model.UserModel;
import com.kariqu.tyt.http.pkg.*;
import com.kariqu.tyt.http.redis.Redis;
import com.kariqu.tyt.http.service.ConfigCachedService;
import com.kariqu.tyt.http.service.MissionCachedService;
import com.kariqu.tyt.http.service.UserCachedService;
import com.kariqu.tyt.http.service.UserMissionCachedService;
import com.kariqu.tyt.http.task.CoinsLogTask;
import com.kariqu.tyt.http.task.GameRecordTask;
import com.kariqu.tyt.http.task.TaskManager;
import com.kariqu.tyt.http.utility.Utility;
import com.sun.net.httpserver.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("tyt/game/v1")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private UserCachedService userCachedService;

    @Autowired
    private UserMissionCachedService userMissionCachedService;

    @Autowired
    private MissionCachedService missionCachedService;

    @Autowired
    private ConfigCachedService configCachedService;

    @PostMapping(value = "/push")
    public ResponseData push(@RequestBody(required = true) String requestBody) {
        try {
            ReqPush req = Utility.parseJsonToObject(requestBody, ReqPush.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            int userId = req.getUserId();
            UserEntity userEntity = userCachedService.findUserByUserId(userId);
            if (userEntity == null) {
                logger.warn("user entity is null. uid: {}", userId);
                return ResponseError.ContentInvalid;
            }

            // 任务
            List<UserMissionEntity> userMissionEntityList = userMissionCachedService.findUserMission(userEntity.getUserId());
            if (userMissionEntityList == null) {
                logger.warn("can't find user mission. userId: {}", userId);
                return ResponseError.UnknownError;
            }

            List<Integer> finishedMission = new ArrayList<>();
            boolean saveUserMission = false;
            for (UserMissionEntity userMissionEntity : userMissionEntityList) {
                if (processUserMission_Push(req, userMissionEntity, finishedMission)) {
                    saveUserMission = true;
                }
            }
            if (saveUserMission) {
                userMissionCachedService.asyncSaveUserMissions(userId, userMissionEntityList);
            }
            return new ResponseData().put("mission", finishedMission);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/save_score")
    public ResponseData save_score(@RequestBody(required = true) String requestBody) {
        try {
            ReqSaveScore req = Utility.parseJsonToObject(requestBody, ReqSaveScore.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            {
                GameRecordEntity gameRecordEntity = new GameRecordEntity();
                gameRecordEntity.setUserId(req.getUserId());
                gameRecordEntity.setDuration(req.getDuration());
                gameRecordEntity.setTotalCnt(req.getTotalCnt());
                gameRecordEntity.setSuccessCnt(req.getSuccessCnt());
                gameRecordEntity.setTotalScore(req.getScore());
                gameRecordEntity.setRebirthCnt(req.getRebirthCnt());
                gameRecordEntity.setPerfectCnt(req.getPerfectCnt());
                gameRecordEntity.setHeartCnt(req.getHeartCnt());
                TaskManager.getInstace().put(new GameRecordTask(gameRecordEntity));
            }

            UserEntity userEntity = userCachedService.findUserByUserId(req.getUserId());
            if (userEntity == null) {
                logger.warn("can't find user by userid: {} ", req.getUserId());
                return ResponseError.ContentInvalid;
            }

            if (userEntity.getScore() > req.getScore()) {
                return ResponseError.Success;
            }

            logger.info("save score. uid: {} body: {}", req.getUserId(), requestBody);

            userEntity.setScore(req.getScore());
            userEntity = userCachedService.asyncSaveEntity(userEntity);
            return ResponseError.Success;
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/song")
    public ResponseData song(@RequestBody(required = true) String requestBody) {
        try {
            ReqSong req = Utility.parseJsonToObject(requestBody, ReqSong.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            int userId = req.getUserId();
            UserEntity userEntity = userCachedService.findUserByUserId(userId);
            if (userEntity == null)
                return ResponseError.ContentInvalid;

            // TODO 点歌默认扣除多少
            int cost = configCachedService.getValueAsInteger(ConfigEntity.ID_DIAN_GE_COST);
            if (cost <= 0)
                cost = 5;
            if (userEntity.getCoins() < cost) {
                return ResponseError.CoinsInsufficient;
            }

            userEntity.changeCoins(-cost);
            userEntity = userCachedService.asyncSaveEntity(userEntity);
            {
                CoinsLogEntity entity = new CoinsLogEntity();
                entity.setUserId(userId);
                entity.setCoinsChange(-cost);
                entity.setCoinsReamin(userEntity.getCoins());
                entity.setType(CoinsLogEntity.TYPE_SONG);
                TaskManager.getInstace().put(new CoinsLogTask(entity));
            }

            // 任务
            List<UserMissionEntity> userMissionEntityList = userMissionCachedService.findUserMission(userId);
            if (userMissionEntityList == null) {
                logger.warn("can't find user mission. userId: {}", userId);
                return ResponseError.UnknownError;
            }
            List<Integer> finishedMission = new ArrayList<>();
            boolean saveUserMission = false;
            for (UserMissionEntity userMissionEntity : userMissionEntityList) {
                if (processUserMission_DianGe(userMissionEntity, finishedMission)) {
                    saveUserMission = true;
                }
            }
            if (saveUserMission) {
                userMissionCachedService.asyncSaveUserMissions(userId, userMissionEntityList);
            }

            logger.info("song . uid: {} body: {}", req.getUserId(), requestBody);

            return new ResponseData().put("mission", finishedMission);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/user_info")
    public ResponseData user_info(@RequestBody(required = true) String requestBody) {
        try {
            ReqUserInfo req = Utility.parseJsonToObject(requestBody, ReqUserInfo.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            UserEntity userEntity = userCachedService.findUserByUserId(req.getUserId());
            if (userEntity == null)
                return ResponseError.ContentInvalid;
            UserModel userModel = new UserModel(userEntity);
            return new ResponseData().put("user", userModel);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/rebirth")
    public ResponseData rebirth(@RequestBody(required = true) String requestBody) {
        try {
            ReqRebirth req = Utility.parseJsonToObject(requestBody, ReqRebirth.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            int userId = req.getUserId();
            UserEntity userEntity = userCachedService.findUserByUserId(userId);
            if (userEntity == null) {
                logger.warn("user is null. uid: {}", userId);
                return ResponseError.UnknownError;
            }

            {
                CoinsLogEntity entity = new CoinsLogEntity();
                entity.setType(CoinsLogEntity.TYPE_REBIRTH);
                entity.setUserId(userId);
                entity.setCoinsChange(0);
                entity.setCoinsReamin(userEntity.getCoins());
                TaskManager.getInstace().put(new CoinsLogTask(entity));
            }

            // 任务
            List<UserMissionEntity> userMissionEntityList = userMissionCachedService.findUserMission(userId);
            if (userMissionEntityList == null) {
                logger.warn("can't find user mission. userId: {}", userId);
                return ResponseError.UnknownError;
            }

            List<Integer> finishedMission = new ArrayList<>();
            boolean saveUserMission = false;
            for (UserMissionEntity userMissionEntity : userMissionEntityList) {
                if (processUserMission_Rebirth(userMissionEntity, finishedMission)) {
                    saveUserMission = true;
                }
            }
            if (saveUserMission) {
                userMissionCachedService.asyncSaveUserMissions(userId, userMissionEntityList);
            }

            logger.info("rebirth . uid: {} body: {}", req.getUserId(), requestBody);

            return new ResponseData().put("mission", finishedMission);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    private boolean processUserMission_Push(ReqPush reqPush, UserMissionEntity userMissionEntity, List<Integer> finishedMission) {
        MissionEntity missionEntity = missionCachedService.findMission(userMissionEntity.getMissionId());
        if (missionEntity == null) {
            logger.warn("can't find mission. mission_id: {}", userMissionEntity.getMissionId());
            return false;
        }

        if (userMissionEntity.getState() != UserMissionEntity.STATE_UNFINISH) {
            return false;
        }

        if (missionEntity.getType() == MissionEntity.TYPE_PAI_JIU) {
            return processUserMission_PaiJiu(reqPush, userMissionEntity, missionEntity, finishedMission);
        } else if (missionEntity.getType() == MissionEntity.TYPE_NPC_PAI_JIU) {
            return processUserMission_NpcPaiJiu(reqPush, userMissionEntity, missionEntity, finishedMission);
        } else if (missionEntity.getType() == MissionEntity.TYPE_BAR) {
            return processUserMission_Bar(reqPush, userMissionEntity, missionEntity, finishedMission);
        }
        return false;
    }

    private boolean processUserMission_Rebirth(UserMissionEntity userMissionEntity, List<Integer> finishedMission) {
        MissionEntity missionEntity = missionCachedService.findMission(userMissionEntity.getMissionId());
        if (missionEntity == null) {
            logger.warn("can't find mission. mission_id: {}", userMissionEntity.getMissionId());
            return false;
        }
        if (userMissionEntity.getState() != UserMissionEntity.STATE_UNFINISH) {
            return false;
        }
        if (missionEntity.getType() == MissionEntity.TYPE_REBIRTH) {
            if (addUserMissionGoal(userMissionEntity, missionEntity))
                finishedMission.add(userMissionEntity.getMissionId());
            return true;
        }
        return false;
    }

    private boolean processUserMission_DianGe(UserMissionEntity userMissionEntity, List<Integer> finishedMission) {
        MissionEntity missionEntity = missionCachedService.findMission(userMissionEntity.getMissionId());
        if (missionEntity == null) {
            logger.warn("can't find mission. mission_id: {}", userMissionEntity.getMissionId());
            return false;
        }
        if (userMissionEntity.getState() != UserMissionEntity.STATE_UNFINISH) {
            return false;
        }
        if (missionEntity.getType() == MissionEntity.TYPE_DIAN_GE) {
            if (addUserMissionGoal(userMissionEntity, missionEntity))
                finishedMission.add(userMissionEntity.getMissionId());
            return true;
        }
        return false;
    }

    private boolean processUserMission_PaiJiu(ReqPush reqPush, UserMissionEntity userMissionEntity
            , MissionEntity missionEntity, List<Integer> finishedMission) {
        if (reqPush.getScore() <= 0)
            return false;
        if (addUserMissionGoal(userMissionEntity, missionEntity))
            finishedMission.add(userMissionEntity.getMissionId());
        return true;
    }

    private boolean processUserMission_NpcPaiJiu(ReqPush reqPush, UserMissionEntity userMissionEntity
            , MissionEntity missionEntity, List<Integer> finishedMission) {
        if (reqPush.getScore() <= 0)
            return false;
        if (reqPush.getNpcId() !=  missionEntity.getAttr1()) {
            return false;
        }
        if (addUserMissionGoal(userMissionEntity, missionEntity))
            finishedMission.add(userMissionEntity.getMissionId());
        return true;
    }

    private boolean processUserMission_Bar(ReqPush reqPush, UserMissionEntity userMissionEntity
            , MissionEntity missionEntity, List<Integer> finishedMission) {
        if (reqPush.getScore() <= 0)
            return false;
        if (reqPush.getBarId() !=  missionEntity.getAttr1()) {
            return false;
        }
        if (addUserMissionGoal(userMissionEntity, missionEntity))
            finishedMission.add(userMissionEntity.getMissionId());
        return true;
    }

    private boolean addUserMissionGoal(UserMissionEntity userMissionEntity, MissionEntity missionEntity) {
        userMissionEntity.setCurrentGoal(userMissionEntity.getCurrentGoal() + 1);
        if (userMissionEntity.getCurrentGoal() > missionEntity.getGoal())
            userMissionEntity.setCurrentGoal(missionEntity.getGoal());
        if (userMissionEntity.getCurrentGoal() == missionEntity.getGoal()) {
            userMissionEntity.setState(UserMissionEntity.STATE_UNREWARD);
            return true;
        }
        return false;
    }
}
