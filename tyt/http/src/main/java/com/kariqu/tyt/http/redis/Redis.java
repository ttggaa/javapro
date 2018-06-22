package com.kariqu.tyt.http.redis;

import com.google.gson.Gson;
import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.common.persistence.entity.UserMissionEntity;
import com.kariqu.tyt.common.persistence.redis.BaseRedis;
import com.kariqu.tyt.http.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Redis extends BaseRedis {
    private static final Logger logger = LoggerFactory.getLogger(Redis.class);


    public static class UserMissionJson
    {
        public List<UserMissionEntity> array;
    }

    public Integer getUserIdByOpenId(String openid) {
        try {
            String obj = (String)getRedisTemplate().opsForHash().get(KEY_USERID, openid);
            if (obj == null)
                return null;
            return Integer.valueOf(obj);
        } catch (Exception e) {
            logger.warn("redis exception: {}", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public boolean clearUser(int userId) {
        try {
            long val = getRedisTemplate().opsForHash().delete(KEY_USER, String.valueOf(userId));
            return true;
        } catch (Exception e) {
            logger.warn("redis exception: {}", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearUserTag(int userId) {
        try {
            long val = getRedisTemplate().opsForHash().delete(KEY_USER_TAG, String.valueOf(userId));
            return true;
        } catch (Exception e) {
            logger.warn("redis exception: {}", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public UserEntity getUser(int userId) {
        try {
            String json = (String)getRedisTemplate().opsForHash().get(KEY_USER, String.valueOf(userId));
            if (json == null)
                return null;
            return Utility.parseJsonToObject(json, UserEntity.class);
        } catch (Exception e) {
            logger.warn("redis exception: {}", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveUserId(String openId, int userId) {
        try {
            getRedisTemplate().opsForHash().put(KEY_USERID, openId, String.valueOf(userId));
            return true;
        } catch (Exception e) {
            logger.warn("redis exception: {}", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveUser(UserEntity userEntity) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(userEntity);
            getRedisTemplate().opsForHash().put(KEY_USER, String.valueOf(userEntity.getUserId()), json);
            return true;
        } catch (Exception e) {
            logger.warn("redis exception: {}", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveUserTag(int userId) {
        return saveTag(KEY_USER_TAG, userId);
    }

    public long getUserTagCount() {
        try {
            long cnt = getRedisTemplate().opsForHash().size(KEY_USER_TAG);
            return cnt;
        } catch (Exception e) {
            return -1;
        }
    }

    public void removeUserTag(int userId) {
        try {
            getRedisTemplate().opsForHash().delete(KEY_USER_TAG, String.valueOf(userId));
        } catch (Exception e) {
        }
    }

    public List<UserMissionEntity> getUserMission(int userId) {
        try {
            // 没有任务，可能是新用户，或者任务重置了
            String jsonStr = (String)getRedisTemplate().opsForHash().get(KEY_USER_MISSION, String.valueOf(userId));
            if (jsonStr == null) {
                return new ArrayList<UserMissionEntity>();
            }
            Gson gson = new Gson();
            UserMissionJson userMissionJson = gson.fromJson(jsonStr, UserMissionJson.class);
            if (userMissionJson == null) {
                logger.warn("parse mission json error. {}", jsonStr);
                return null;
            }
            return userMissionJson.array;
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            return null;
        }
    }

    public boolean saveUserMission(int userId, List<UserMissionEntity> array) {
        try {
            UserMissionJson json = new UserMissionJson();
            json.array = array;

            Gson gson = new Gson();
            String jsonString = gson.toJson(json);
            if (jsonString == null)
                return false;
            getRedisTemplate().opsForHash().put(KEY_USER_MISSION, String.valueOf(userId), jsonString);
            return true;
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            return false;
        }
    }

    public boolean saveUserMissionTag(int userId) {
        return saveTag(KEY_USER_MISSION_TAG, userId);
    }

    public long getUserMissionTagCount() {
        try {
            long cnt = getRedisTemplate().opsForHash().size(KEY_USER_MISSION_TAG);
            return cnt;
        } catch (Exception e) {
            return -1;
        }
    }

    public void removeUserMissionTag(int userId) {
        try {
            getRedisTemplate().opsForHash().delete(KEY_USER_MISSION_TAG, String.valueOf(userId));
        } catch (Exception e) {
        }
    }

    public boolean deleteNormalRank() {
        try {
            getRedisTemplate().delete(KEY_NORMAL_RANK);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean saveTag(String tagName, int userId) {
        String tm = "";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
            sb.append(':');
            sb.append(Utility.increment());
            tm = sb.toString();
            getRedisTemplate().opsForHash().put(tagName, String.valueOf(userId), tm);
            return true;
        } catch (Exception e) {
            logger.warn("exception. tagName: {} userId: {} tm: {} {}", tagName, userId, tm, e.toString());
            return false;
        }
    }
}
