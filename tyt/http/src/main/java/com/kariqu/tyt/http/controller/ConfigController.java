package com.kariqu.tyt.http.controller;

import com.kariqu.tyt.common.persistence.entity.ConfigEntity;
import com.kariqu.tyt.common.persistence.entity.SignInEntity;
import com.kariqu.tyt.http.model.ConfigModel;
import com.kariqu.tyt.http.model.SigninConfigModel;
import com.kariqu.tyt.http.pkg.ReqGetConfig;
import com.kariqu.tyt.http.pkg.ReqGetSigninConfig;
import com.kariqu.tyt.http.pkg.ResponseData;
import com.kariqu.tyt.http.pkg.ResponseError;
import com.kariqu.tyt.http.service.ConfigCachedService;
import com.kariqu.tyt.http.service.SigninCachedService;
import com.kariqu.tyt.http.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("tyt/config/v1")
public class ConfigController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private ConfigCachedService configCachedService;

    @Autowired
    private SigninCachedService signinCachedService;

    @PostMapping(value = "/get_config")
    public ResponseData get_config(@RequestBody(required = true) String requestBody) {
        try {
            ReqGetConfig req = Utility.parseJsonToObject(requestBody, ReqGetConfig.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            List<ConfigEntity> all = configCachedService.getAllClientUse();
            List<ConfigModel> respData = new ArrayList<>();
            for (ConfigEntity entity : all) {
                respData.add(new ConfigModel(entity));
            }
            return new ResponseData().put("list", respData);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }

    @PostMapping(value = "/get_signin_config")
    public ResponseData get_signin_config(@RequestBody(required = true) String requestBody) {
        try {
            ReqGetSigninConfig req = Utility.parseJsonToObject(requestBody, ReqGetSigninConfig.class);
            if (req == null || !req.isValid()) {
                logger.warn("req invalid. body: {}", requestBody);
                return ResponseError.ContentInvalid;
            }

            List<SignInEntity> all = signinCachedService.findAll();
            List<SigninConfigModel> respData = new ArrayList<>();
            for (SignInEntity entity : all) {
                respData.add(new SigninConfigModel(entity));
            }
            return new ResponseData().put("list", respData);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
            return ResponseError.UnknownError;
        }
    }
}
