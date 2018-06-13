package com.kariqu.zwsrv.thelib.account;

import com.kariqu.zwsrv.thelib.cache.BaseRedisHashCache;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 23/06/17.
 */

@Component("redisAccountCache")
public class RedisAccountCache extends BaseRedisHashCache<Account> {

    @Override
    public String cacheKey() {
        return "zwsrv:_account";
    }

    public void put(Account account) {
        put(String.valueOf(account.getUserId()),account);
    }

    public void putAll(List<Account> accounts) {
        Map<String,Account> map = new HashMap<>();
        for (Account account : accounts) {
            map.put(String.valueOf(account.getUserId()),account);
        }
        putAll(map);
    }

}
