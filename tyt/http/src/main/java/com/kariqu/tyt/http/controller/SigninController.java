package com.kariqu.tyt.http.controller;

import com.kariqu.tyt.common.persistence.entity.CoinsLogEntity;
import com.kariqu.tyt.common.persistence.entity.SignInEntity;
import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.common.persistence.service.SignInService;
import com.kariqu.tyt.http.pkg.ReqGetSignin;
import com.kariqu.tyt.http.pkg.ReqSignin;
import com.kariqu.tyt.http.pkg.ResponseData;
import com.kariqu.tyt.http.pkg.ResponseError;
import com.kariqu.tyt.http.service.SigninCachedService;
import com.kariqu.tyt.http.service.UserCachedService;
import com.kariqu.tyt.http.task.CoinsLogTask;
import com.kariqu.tyt.http.task.TaskManager;
import com.kariqu.tyt.http.utility.LocalDateTimeUtils;
import com.kariqu.tyt.http.utility.Utility;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("tyt/signin/v1")
public class SigninController {
    private static final Logger logger = LoggerFactory.getLogger(SigninController.class);

    @Autowired
    private SigninCachedService signinCachedService;

    @Autowired
    private UserCachedService userCachedService;

    @PostMapping(value = "/signin")
    public ResponseData signin(@RequestBody(required = true) String requestBody) {
        try {
            ReqSignin reqSignin = Utility.parseJsonToObject(requestBody, ReqSignin.class);
            if (reqSignin == null || !reqSignin.isValid()) {
                logger.warn("req invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            UserEntity userEntity = userCachedService.findUserByUserId(reqSignin.getUserId());
            if (userEntity == null) {
                return ResponseError.UnknownError;
            }

            // 今天签到
            LocalDateTime tnow = LocalDateTime.now();
            LocalDateTime signinTm = LocalDateTimeUtils.dateToLocalDateTime(userEntity.getSigninTm());
            if (LocalDateTimeUtils.isSameDay(signinTm, tnow)) {
                return ResponseError.RepeatedSignin;
            }

            if (userEntity.getSigninId() >= SignInEntity.MAX) {
                userEntity.setSigninId(0);
            }

            /*
            // 昨天签到
            if (LocalDateTimeUtils.isSameDay(signinTm.plusDays(1), tnow)) {
                // 超过最大了，重新开始
                if (userEntity.getSigninId() == SignInEntity.MAX) {
                    userEntity.setSigninId(0);
                }
            } else {
                userEntity.setSigninId(0);
            }
            */

            int nextId = userEntity.getSigninId() + 1;
            SignInEntity signInEntity = signinCachedService.findEntity(nextId);
            if (signInEntity == null) {
                logger.warn("can't find sigin id: {} uid: {}", nextId, userEntity.getUserId());
                return ResponseError.UnknownError;
            }

            Date now = new Date();
            userEntity.setSigninId(nextId);
            userEntity.setSigninTm(now);
            userEntity.changeCoins(signInEntity.getCoins());
            userCachedService.asyncSaveEntity(userEntity);

            {
                CoinsLogEntity coinsLogEntity = new CoinsLogEntity();
                coinsLogEntity.setType(CoinsLogEntity.TYPE_SIGIN_REWARD);
                coinsLogEntity.setCoinsReamin(userEntity.getCoins());
                coinsLogEntity.setCoinsChange(signInEntity.getCoins());
                coinsLogEntity.setUserId(userEntity.getUserId());
                TaskManager.getInstace().put(new CoinsLogTask(coinsLogEntity));
            }
            return ResponseError.Success;
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }
}
