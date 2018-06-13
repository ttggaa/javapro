package com.kariqu.zwsrv.app.cache;

import com.kariqu.zwsrv.app.model.GameRewardInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by simon on 09/01/2018.
 */
@Component("redisExpireRoomRewardCache")
public class RedisExpireRoomRewardCache  extends BaseRedisListCache<GameRewardInfo> {

    public static final String _cacheKey   = "zww:expire:_room_rewards";

    @Override
    public String cacheKey() {
        return _cacheKey;
    }

    protected String cacheKey(int roomId) {
        return cacheKey()+":"+roomId;
    }

    public List<GameRewardInfo> range(int roomId, long start, long end) {
        return range(cacheKey(roomId),start,end);
    }

    public long leftPush(int roomId, GameRewardInfo value) {
        return leftPush(cacheKey(roomId),value);
    }

    public GameRewardInfo rightPop(int roomId) {
        return rightPop(cacheKey(roomId));
    }
}
