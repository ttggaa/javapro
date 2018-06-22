package com.kariqu.tyt.http.controller;

import com.kariqu.tyt.common.persistence.entity.*;
import com.kariqu.tyt.common.persistence.service.UserService;
import com.kariqu.tyt.http.model.UserModel;
import com.kariqu.tyt.http.model.WechatAuthModel;
import com.kariqu.tyt.http.pkg.*;
import com.kariqu.tyt.http.service.*;
import com.kariqu.tyt.http.task.CoinsLogTask;
import com.kariqu.tyt.http.task.TaskManager;
import com.kariqu.tyt.http.utility.LocalDateTimeUtils;
import com.kariqu.tyt.http.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("tyt/login/v1")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserCachedService userService;

    @Autowired
    private WeChatAuthService weChatAuthService;

    @Autowired
    private MissionCachedService missionService;

    @Autowired
    private UserMissionCachedService userMissionService;

    @Autowired
    private ConfigCachedService configCachedService;

    @PostMapping(value = "/wechat_auth")
    public ResponseData wechat_auth(@RequestBody(required = true) String requestBody) {
        try {
            ReqWechatAuth req = Utility.parseJsonToObject(requestBody, ReqWechatAuth.class);
            if (req == null || !req.isValid()) {
                logger.warn("req is invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            WeChatAuthService.WechatSession session = weChatAuthService.getAccessToken(req.getCode());
            if (session == null)
                return ResponseError.WechatSessionError;
            if (session.getErrcode() != 0) {
                String errMsg = session.getErrMsg() != null ? session.getErrMsg() : "";
                logger.warn("wechat session error: {}. errmsg: {}", session.getErrcode(), errMsg);
                return ResponseError.WechatSessionError;
            }

            WechatAuthModel resp = new WechatAuthModel();
            resp.setSession_key(session.getSession_key());
            resp.setOpenid(session.getOpenid());
            return new ResponseData().put("result", resp);
        } catch (Exception e) {
            logger.warn("exception. {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/wechat_login")
    public ResponseData wechat_login(@RequestBody(required = true) String requestBody) {
        try {
            ReqWechatLogin req = Utility.parseJsonToObject(requestBody, ReqWechatLogin.class);
            if (req == null || !req.isValid()) {
                logger.warn("req is invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            String openId = req.getOpenId();
            Optional<UserEntity> opt = userService.findIfExist(openId);
            if (opt == null) {
                logger.warn("findUserByOpenId exception. openid: {}", openId);
                return ResponseError.UnknownError;
            }

            ResponseData responseData = null;
            if (opt.isPresent()) {
                UserEntity userEntity = opt.get();
                responseData = userLogin(req, userEntity);
            } else {
                responseData = registerUser(req);
            }
            return responseData;
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/tourist_login")
    public ResponseData tourist_login(@RequestBody(required = true) String requestBody) {
        try {
            ReqTouristLogin req = Utility.parseJsonToObject(requestBody, ReqTouristLogin.class);
            if (req == null || !req.isValid()) {
                logger.warn("req is invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            Optional<UserEntity> opt = userService.findIfExist(req.getOpenid());
            if (opt == null) {
                logger.warn("findUserByOpenId exception. openid: {}", req.getOpenid());
                return ResponseError.UnknownError;
            }

            ReqWechatLogin fakeReq = new ReqWechatLogin();
            fakeReq.setOpenId(req.getOpenid());
            fakeReq.setNickName("呵呵呵");

            ResponseData responseData = null;
            if (opt.isPresent()) {
                UserEntity userEntity = opt.get();
                responseData = userLogin(fakeReq, userEntity);
            } else {
                responseData = registerUser(fakeReq);
            }
            return responseData;
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    private ResponseData userLogin(ReqWechatLogin req, UserEntity userEntity) {
        try {
            int userId = userEntity.getUserId();

            // 任务是否需要刷新
            boolean refreshMission = needRefreshMission(userEntity);
            if (refreshMission) {
                List<UserMissionEntity> userMissionEntityList = randUserMission(userEntity.getUserId());
                userMissionService.asyncSaveUserMissions(userEntity.getUserId(), userMissionEntityList);
                userEntity.setMissionRefreshTm(new Date());
            }

            // TODO 点开签到页面
            // 今天签到
            LocalDateTime tnow = LocalDateTime.now();
            LocalDateTime signinTm = LocalDateTimeUtils.dateToLocalDateTime(userEntity.getSigninTm());

            // 今天
            if (LocalDateTimeUtils.isSameDay(signinTm, tnow)) {

            } else if (LocalDateTimeUtils.isSameDay(signinTm.plusDays(1), tnow)) {
                // 昨天签到, 超过最大了，重新开始
                if (userEntity.getSigninId() == SignInEntity.MAX) {
                    userEntity.setSigninId(0);
                }
            } else {
                // 不是今天，不是昨天
                userEntity.setSigninId(0);
            }

            userEntity.setNickname(req.getNickName());
            userEntity = userService.asyncSaveEntity(userEntity);

            logger.info("user login. uid: {} refreshMision: {}", userId, refreshMission?1:0);

            UserModel respData = new UserModel(userEntity);
            return new ResponseData().put("user", respData);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.LoginError;
        }
    }

    private ResponseData registerUser(ReqWechatLogin req) {
        try {
            // 先注册
            UserEntity userEntity = new UserEntity();
            userEntity.setWechatOpenid(req.getOpenId());
            if (req.getNickName() != null)
                userEntity.setNickname(req.getNickName());
            int val = configCachedService.getValueAsInteger(ConfigEntity.ID_REGISTER_COINS);
            userEntity.setRegisterCoins(val);
            userEntity = userService.syncInsertEntity(userEntity);

            int userId = userEntity.getUserId();
            {
                CoinsLogEntity entity = new CoinsLogEntity();
                entity.setUserId(userId);
                entity.setType(CoinsLogEntity.TYPE_REGISTER);
                entity.setCoinsChange(0);
                entity.setCoinsReamin(userEntity.getCoins());
                TaskManager.getInstace().put(new CoinsLogTask(entity));
            }

            // 分配任务
            List<UserMissionEntity> userMissionEntityList = randUserMission(userEntity.getUserId());
            userMissionService.asyncSaveUserMissions(userEntity.getUserId(), userMissionEntityList);
            userEntity.setMissionRefreshTm(new Date());
            userService.asyncSaveEntity(userEntity);

            logger.info("user register uid: {}", userId);

            UserModel respData = new UserModel(userEntity, true);
            return new ResponseData().put("user", respData);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.LoginError;
        }
    }

    private List<UserMissionEntity> randUserMission(int userId) {
        final int count = UserMissionEntity.USER_MISSION_MAX;
        List<MissionEntity> missionEntityList = missionService.randomMission(count);

        List<UserMissionEntity> userMissionEntityList = new ArrayList<>();
        for (int i = 0; i != count; ++i) {
            MissionEntity missionEntity = missionEntityList.get(i);

            UserMissionEntity entity = new UserMissionEntity();
            entity.setUserId(userId);
            entity.setIndex(i);
            entity.setMissionId(missionEntity.getMissionId());
            userMissionEntityList.add(entity);
        }
        return userMissionEntityList;
    }

    public boolean needRefreshMission(UserEntity userEntity) {
        LocalDateTime previous = LocalDateTimeUtils.dateToLocalDateTime(userEntity.getMissionRefreshTm());
        LocalDateTime now = LocalDateTime.now();
        return !LocalDateTimeUtils.isSameDay(previous, now);
    }
}
