package com.kariqu.tyt.common.persistence.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class BaseRedis {

    // 用户数据 hash userid: json
    public static final String KEY_USER = "user";
    public static final String KEY_USER_TAG = "user_tag";

    // 用户任务hash userid: json
    public static final String KEY_USER_MISSION = "user_mission";
    public static final String KEY_USER_MISSION_TAG = "user_mission_tag";

    // hash openid: userid
    public static final String KEY_USERID = "userid";

    public static final String KEY_NORMAL_RANK = "normal_rank";
    public static final String KEY_NORMAL_RANK_TAG = "normal_rank_tag";


    @Autowired
    protected StringRedisTemplate redisTemplate;

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
