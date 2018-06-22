package com.kariqu.tyt.common.persistence.repository;

import com.kariqu.tyt.common.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public List<UserEntity> findAllByWechatOpenid(String openid);
    public UserEntity findUserEntityByUserId(int userId);
}
