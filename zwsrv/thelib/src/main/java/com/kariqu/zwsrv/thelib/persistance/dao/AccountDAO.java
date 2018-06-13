package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 14/02/17.
 */
public interface AccountDAO extends JpaRepository<Account, Integer> {
}

