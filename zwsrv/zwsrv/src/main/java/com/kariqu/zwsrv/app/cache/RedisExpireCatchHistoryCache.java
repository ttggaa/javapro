package com.kariqu.zwsrv.app.cache;

import com.kariqu.zwsrv.app.model.CatchHistoryInfo;
import org.springframework.stereotype.Component;


@Component("redisExpireCatchHistoryCache")
public class RedisExpireCatchHistoryCache extends BaseRedisListCache<CatchHistoryInfo> {
    public static final String _cacheKey   = "zww:expire:_catch_history";

    @Override
    public String cacheKey() {
        return _cacheKey;
    }
}
