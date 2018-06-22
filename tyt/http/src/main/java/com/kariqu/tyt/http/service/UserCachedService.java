package com.kariqu.tyt.http.service;

import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.common.persistence.service.UserService;
import com.kariqu.tyt.http.redis.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCachedService extends UserService {

    @Autowired
    private Redis redis;

    public Optional<UserEntity> findIfExist(String openId) {
        Integer userId = redis.getUserIdByOpenId(openId);
        if (userId == null) {
            Optional<UserEntity> userEntityOptional = super.findByOpenId(openId);
            if (userEntityOptional == null)
                return null;
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                redis.saveUserId(userEntity.getWechatOpenid(), userEntity.getUserId());
                redis.saveUser(userEntity);
            }
            return userEntityOptional;
        }

        UserEntity userEntity = redis.getUser(userId);
        if (userEntity == null) {
            Optional<UserEntity> userEntityOptional = super.findByOpenId(openId);
            if (userEntityOptional == null)
                return null;
            if (userEntityOptional.isPresent()) {
                userEntity = userEntityOptional.get();
                redis.saveUserId(userEntity.getWechatOpenid(), userEntity.getUserId());
                redis.saveUser(userEntity);
            }
        }
        return Optional.ofNullable(userEntity);
    }

    @Override
    public UserEntity findUserByUserId(int userId) {
        UserEntity userEntity = redis.getUser(userId);
        if (userEntity == null) {
            userEntity = super.findUserByUserId(userId);
            if (userEntity != null) {
                redis.saveUser(userEntity);
            }
        }
        return userEntity;
    }

    @Override
    public UserEntity findUserByOpenId(String openid) {
        Integer userId = redis.getUserIdByOpenId(openid);
        if (userId == null) {
            UserEntity userEntity = super.findUserByOpenId(openid);
            if (userEntity != null) {
                redis.saveUserId(userEntity.getWechatOpenid(), userEntity.getUserId());
            }
            return userEntity;
        }

        UserEntity userEntity = redis.getUser(userId);
        if (userEntity == null) {
            userEntity = super.findUserByOpenId(openid);
            if (userEntity != null) {
                redis.saveUser(userEntity);
            }
        }
        return userEntity;
    }

    public UserEntity syncInsertEntity(UserEntity userEntity) {
        userEntity = super.syncInsertEntity(userEntity);
        redis.saveUserId(userEntity.getWechatOpenid(), userEntity.getUserId());
        redis.saveUser(userEntity);
        return userEntity;
    }

    public UserEntity asyncSaveEntity(UserEntity userEntity) {
        redis.saveUser(userEntity);
        redis.saveUserTag(userEntity.getUserId());
        return userEntity;
    }

}
