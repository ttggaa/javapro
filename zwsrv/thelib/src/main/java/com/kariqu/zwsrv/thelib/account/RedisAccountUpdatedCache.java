package com.kariqu.zwsrv.thelib.account;

import com.kariqu.zwsrv.thelib.cache.BaseRedisHashCache;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import org.springframework.stereotype.Component;

/**
 * Created by simon on 22/05/2018.
 */

@Component("redisAccountUpdatedCache")
public class RedisAccountUpdatedCache extends BaseRedisHashCache<Account> {

    @Override
    public String cacheKey() {
        return "zwsrv:_account_updated";
    }

    public void put(Account account) {
        put(String.valueOf(account.getUserId()),account);
    }
}
