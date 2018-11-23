package com.cgy.test.demo.persistence.daoweb;

import com.cgy.test.demo.persistence.entity.UserBasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBasicDaoweb  extends JpaRepository<UserBasicEntity, Integer> {

}

