//package com.kariqu.zwsrv.app.cache;
//
//import org.springframework.data.redis.core.RedisTemplate;
//
//import javax.annotation.Resource;
//
///**
// * Created by simon on 24/06/17.
// */
//public abstract class BaseRedisZSetCache<T> {
//
//    @Resource(name = "redisTemplate")
//    protected RedisTemplate<String, String> redisTemplate;
//
//    public abstract String cacheKey();
//
//
//    public void clear() {
//        redisTemplate.getConnectionFactory().getConnection().flushDb();
//    }
//
//    protected RedisTemplate<String, String> getRedisTemplate() {
//        return redisTemplate;
//    }
//
//    public void put(String key, T value) {
////        getRedisTemplate().opsForZSet().getOperations()
////        getRedisTemplate().opsForZSet().add()
//        getRedisTemplate().opsForHash().put(cacheKey(),key,value);
//    }
//
//}
//
//
////http://www.jianshu.com/p/7b70860d33bf
////http://www.2cto.com/database/201605/506456.html
////http://blog.nosqlfan.com/html/2938.html
////http://m.blog.csdn.net/hanierming/article/details/52489969
//
