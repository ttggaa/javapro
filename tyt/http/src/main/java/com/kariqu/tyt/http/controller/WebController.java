package com.kariqu.tyt.http.controller;


import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.http.ApplicationConfig;
import com.kariqu.tyt.http.model.WebChangeCoinModel;
import com.kariqu.tyt.http.pkg.ResponseData;
import com.kariqu.tyt.http.pkg.ResponseError;
import com.kariqu.tyt.http.pkg.WebReqChangeCoin;
import com.kariqu.tyt.http.pkg.WebReqClearUser;
import com.kariqu.tyt.http.redis.Redis;
import com.kariqu.tyt.http.service.UserCachedService;
import com.kariqu.tyt.http.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tyt/web")
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private UserCachedService userCachedService;

    @Autowired
    private Redis redis;

    @PostMapping(value = "/change_coin")
    public ResponseData change_coin(@RequestBody(required = true) String requestBody) {
        try {
            WebReqChangeCoin req = Utility.parseJsonToObject(requestBody, WebReqChangeCoin.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }
            final String webKey = ApplicationConfig.getInstance().getWebKey();
            if (!req.getKey().equals(webKey)) {
                logger.warn("req key error. key: {}", req.getKey());
                return ResponseError.ContentInvalid;
            }

            int userId = req.getUserId();
            UserEntity userEntity = userCachedService.findUserByUserId(userId);
            if (userEntity == null) {
                logger.warn("can't find user. uid: {}", userId);
                return ResponseError.UnknownError;
            }
            int previousCoins = userEntity.getCoins();
            userEntity.changeCoins(req.getCoins());
            userCachedService.asyncSaveEntity(userEntity);

            WebChangeCoinModel model = new WebChangeCoinModel();
            model.setUserId(userId);
            model.setPreviousCoin(previousCoins);
            model.setChangeCoin(req.getCoins());
            model.setRemainCoin(userEntity.getCoins());

            return new ResponseData().put("result", model);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/clear_user")
    public ResponseData clear_user(@RequestBody(required = true) String requestBody) {
        try {
            WebReqClearUser req = Utility.parseJsonToObject(requestBody, WebReqClearUser.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            final String webKey = ApplicationConfig.getInstance().getWebKey();
            if (!req.getKey().equals(webKey)) {
                logger.warn("req key error. key: {} {}", req.getKey(), webKey);
                return ResponseError.ContentInvalid;
            }

            boolean retUser = false;
            boolean retUserTag = false;
            retUser = redis.clearUser(req.getUserId());
            if (retUser)
                retUserTag = redis.clearUserTag(req.getUserId());
            return new ResponseData().put("user", retUser ?1:0).put("user_tag", retUserTag?1 : 0);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

}
