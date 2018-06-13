package com.kariqu.zwsrv.app.cache;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by simon on 7/8/16.
 */
public abstract class BaseRedisHashCache<T> {

    @Resource(name = "redisTemplate")
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     *
     * @Title: clear
     * @Description: TODO(清空数据)
     */
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

    public void put(int key, T value) {
        getRedisTemplate().opsForHash().put(cacheKey(), key,value);
    }

    public void put(String key, T value) {
        getRedisTemplate().opsForHash().put(cacheKey(), key,value);
    }

    public void putAll(Map<String, T> map) {
        getRedisTemplate().opsForHash().putAll(cacheKey(), map);
    }

    public List<T> multiGet(Iterable<Integer> keys) {
        List<Object> keysTemp = new ArrayList<>();
        Iterator it = keys.iterator();
        while(it.hasNext()){
            keysTemp.add(String.valueOf(it.next()));
        }
        List<Object> objects = getRedisTemplate().opsForHash().multiGet(cacheKey(), keysTemp);
        objects.removeAll(Collections.singleton(null));
        return (List<T>)objects;
    }

    public List<T> multiGet(List<Integer> keys) {
        List<Object> keysTemp = new ArrayList<>();
        for (Integer id : keys) {
            keysTemp.add(String.valueOf(id));
        }
        List<Object> objects = getRedisTemplate().opsForHash().multiGet(cacheKey(), keysTemp);
        List<T> result = new ArrayList<>();
        for (Object o : objects) {
            if (o!=null) {
                result.add((T)o);
            }
        }
        objects.removeAll(Collections.singleton(null));
        return (List<T>)objects;
    }

    public T get(int keyId) {
        Object o = getRedisTemplate().opsForHash().get(cacheKey(), String.valueOf(keyId));
        if (o!=null) {
            return (T)o;
        }
        return null;
    }

    public T get(String key) {
        Object o = (T)getRedisTemplate().opsForHash().get(cacheKey(), key);
        if (o!=null) {
            return (T)o;
        }
        return null;
    }

    public void evict(Integer key) {
        getRedisTemplate().opsForHash().delete(cacheKey(), String.valueOf(key));
    }

    public void evict(String key) {
        getRedisTemplate().opsForHash().delete(cacheKey(), key);
    }

    public void evictAll(List<String> keys) {
        getRedisTemplate().opsForHash().delete(cacheKey(), keys.toArray());
    }

    public void evictAll() {

    }

}
