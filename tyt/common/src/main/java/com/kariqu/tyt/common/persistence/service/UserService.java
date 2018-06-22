package com.kariqu.tyt.common.persistence.service;

import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.common.persistence.repository.UserRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserEntity findUserByUserId(int userId) {
        try {
            return getUserRepository().findUserEntityByUserId(userId);
        } catch (Exception e) {
            return null;
        }
    }

    public UserEntity findUserByOpenId(String openid) {
        try {
            List<UserEntity> arr = userRepository.findAllByWechatOpenid(openid);
            if (arr.isEmpty())
                return null;
            return arr.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public UserEntity syncInsertEntity(UserEntity userEntity) {
        return getUserRepository().save(userEntity);
    }

    public UserEntity syncSaveEntity(UserEntity userEntity) {
        return getUserRepository().save(userEntity);
    }

    protected Optional<UserEntity> findByOpenId(String openid) {
        try {
            List<UserEntity> arr = userRepository.findAllByWechatOpenid(openid);
            if (arr.isEmpty())
                return Optional.ofNullable(null);
            return Optional.ofNullable(arr.get(0));
        } catch (Exception e) {
            return null;
        }
    }
}
