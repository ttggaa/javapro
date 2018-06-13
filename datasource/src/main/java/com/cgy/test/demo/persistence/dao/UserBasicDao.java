package com.cgy.test.demo.persistence.dao;

import com.cgy.test.demo.persistence.entity.UserBasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserBasicDao extends JpaRepository<UserBasicEntity, Integer> {

    @Query
    UserBasicEntity findByUserId(int userId);

}
