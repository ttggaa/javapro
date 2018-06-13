package com.kariqu.zwsrv.app.cache;

import com.kariqu.zwsrv.app.model.GameRewardInfo;
import org.springframework.stereotype.Component;

/**
 * Created by simon on 29/12/2017.
 */
@Component("redisExpireGameRewardCache")
public class RedisExpireGameRewardCache extends BaseRedisListCache<GameRewardInfo> {

    public static final String _cacheKey   = "zww:expire:_game_reward";

    @Override
    public String cacheKey() {
        return _cacheKey;
    }
}
