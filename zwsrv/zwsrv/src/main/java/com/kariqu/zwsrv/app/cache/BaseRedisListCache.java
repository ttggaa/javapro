package com.kariqu.zwsrv.app.cache;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by simon on 03/01/2018.
 */
public abstract class BaseRedisListCache<T> {

    @Resource(name = "redisTemplate")
    protected RedisTemplate<String, Object> redisTemplate;

    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    protected RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void fireEvent(String channel, Object message) {
        getRedisTemplate().convertAndSend(channel,message);
    }


    public abstract String cacheKey();

    public List<T> range(long start, long end) {
        return range(cacheKey(),start,end);
    }

    public List<T> range(String cacheKey, long start, long end) {
        List<Object> objects = getRedisTemplate().opsForList().range(cacheKey, start, end);
        List<T> result = new ArrayList<>();
        for (Object o : objects) {
            if (o!=null) {
                result.add((T)o);
            }
        }
        objects.removeAll(Collections.singleton(null));
        return result;
    }

    public long leftPush(T value) {
        return leftPush(cacheKey(),value);
    }

    public long leftPush(String cacheKey, T value) {
        Long size = getRedisTemplate().opsForList().leftPush(cacheKey,value);
        return size!=null?size:0;
    }

    public long leftPush(List<T> value) {
        return leftPush(cacheKey(),value);
    }

    public long leftPush(String cacheKey, List<T> value) {
        Long size = getRedisTemplate().opsForList().leftPushAll(cacheKey, value);
        return size!=null?size:0;
    }

    public T rightPop() {
        return rightPop(cacheKey());
    }

    public T rightPop(String cacheKey) {
        Object obj = getRedisTemplate().opsForList().rightPop(cacheKey);
        return obj!=null?(T)obj:null;
    }

}
