package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 4/26/16.
 */
public interface UserCountDAO extends JpaRepository<UserCount, Integer> {
}
