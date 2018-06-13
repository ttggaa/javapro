package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.AccountDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 16/02/17.
 */
@Service
public class AccountService extends BaseService<Account> {

    @Autowired
    private AccountDAO accountDAO;

    @Override
    protected JpaRepository<Account, Integer> getDao() {
        return accountDAO;
    }


}
