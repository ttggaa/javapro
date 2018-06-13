package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.UserSignin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSigninDAO extends JpaRepository<UserSignin, Integer> {
}
