package com.kariqu.zwsrv.thelib.account;

import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 29/05/2018.
 */
@Service
public class AccountServiceWithCache extends AccountService {

    @Autowired
    RedisAccountCache redisAccountCache;

    public <S extends Account> S saveAndUpdateCache(S entity) {
        S account = super.save(entity);
        if (account!=null) {
            redisAccountCache.put(account);
        }
        return account;
    }

    public Account findOneWithCache(Integer id) {
        Account account = redisAccountCache.get(id);
        if (account==null) {
            account = super.findOne(id);
            if (account!=null) {
                redisAccountCache.put(account);
            }
        }
        return account;
    }

}
