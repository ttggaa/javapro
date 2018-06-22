package com.kariqu.tyt.http.task;

import com.kariqu.tyt.common.persistence.entity.ConfigEntity;
import com.kariqu.tyt.common.persistence.entity.UserEntity;
import com.kariqu.tyt.common.persistence.entity.UserMissionEntity;
import com.kariqu.tyt.common.persistence.service.CoinsLogService;
import com.kariqu.tyt.common.persistence.service.GameRecordService;
import com.kariqu.tyt.http.ApplicationConfig;
import com.kariqu.tyt.http.redis.Redis;
import com.kariqu.tyt.http.service.*;
import com.kariqu.tyt.http.utility.LocalDateTimeUtils;
import com.kariqu.tyt.http.utility.SimpleEventDispatcher;
import com.kariqu.tyt.http.utility.Utility;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskManager extends SimpleEventDispatcher<BaseTask> {
    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private static TaskManager instace;

    private GameRecordService gameRecordService;
    private ConfigCachedService configCachedService;
    private MissionCachedService missionCachedService;
    private SigninCachedService signinCachedService;
    private UserCachedService   userCachedService;
    private UserMissionCachedService userMissionCachedService;
    private CoinsLogService coinsLogService;

    private long cachedRefreshTm;
    private Redis redis;

    public static TaskManager getInstace() {
        return instace;
    }

    @Autowired
    public TaskManager(GameRecordService gameRecordService
            , ConfigCachedService configCachedService
            , MissionCachedService missionCachedService
            , SigninCachedService signinCachedService
            , UserCachedService userCachedService
            , UserMissionCachedService userMissionCachedService
            , CoinsLogService coinsLogService
            , Redis redis) {
        super(5000);
        TaskManager.instace = this;

        this.gameRecordService = gameRecordService;
        this.configCachedService = configCachedService;
        this.missionCachedService = missionCachedService;
        this.signinCachedService = signinCachedService;
        this.userCachedService = userCachedService;
        this.userMissionCachedService = userMissionCachedService;
        this.cachedRefreshTm = System.currentTimeMillis();
        this.coinsLogService = coinsLogService;
        this.redis = redis;

        configCachedService.refreshCached();
        missionCachedService.refreshCached();
        signinCachedService.refreshCached();
    }

    @Override
    protected void dispatchInWorkThread(BaseTask baseTask) {
        try {
            switch (baseTask.getType()) {
                case BaseTask.TYPE_GAME_RECORD: {
                    GameRecordTask gameRecordTask = (GameRecordTask) baseTask;
                    processGameRecordTask(gameRecordTask);
                    return;
                }
                case BaseTask.TYPE_COINS_LOG: {
                    CoinsLogTask coinsLogTask = (CoinsLogTask) baseTask;
                    processCoinsLogTask(coinsLogTask);
                    return;
                }
                default:
                    logger.warn("unknown task type: {}  discard task", baseTask.getType());
            }
        } catch (Exception e) {
            logger.warn("process task exception. type: {} reason: {}", baseTask.getType(), e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void dispatchOnIdle() {
        long tnow = System.currentTimeMillis();
        refreshCahced(tnow);
        //refreshRank(tnow);
        saveTags();
    }

    @Override
    protected void dispatchOnExit() {

    }

    private void processGameRecordTask(GameRecordTask gameRecordTask) {
        gameRecordService.saveEntity(gameRecordTask.getEntity());
    }

    private void processCoinsLogTask(CoinsLogTask coinsLogTask) {
        coinsLogService.saveEntity(coinsLogTask.getEntity());
    }

    // 刷新缓存
    private void refreshCahced(long tnow) {
        try {
            if (tnow - cachedRefreshTm > ApplicationConfig.getInstance().getCachedRefreshSeconds()) {
                configCachedService.refreshCached();
                missionCachedService.refreshCached();
                signinCachedService.refreshCached();
                //logger.info("refresh cached");
                cachedRefreshTm = tnow;
            }
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
        }
    }

    // 刷新排行榜
    private void refreshRank(long tnow) {
        Date date = new Date(tnow);
        LocalDateTime ldt = LocalDateTimeUtils.dateToLocalDateTime(date);
        //logger.info("day of week: {}", ldt.getDayOfWeek());

        // 周一早上0点更新
        if (ldt.getDayOfWeek() != DayOfWeek.MONDAY) {
            return;
        }

        try {
            Date old = configCachedService.getValueAsDate(ConfigEntity.ID_RANK_RESET);
            if (old == null) {
                logger.warn("config find rank reset is null: {} don't reset rank.", ConfigEntity.ID_RANK_RESET);
                return;
            }
            LocalDateTime oldLdt = LocalDateTimeUtils.dateToLocalDateTime(old);
            if (oldLdt.getYear() == ldt.getYear()
                    && oldLdt.getMonth() == ldt.getMonth()
                    && oldLdt.getDayOfMonth() == ldt.getDayOfMonth()) {
                // 今天已经更新过了
                return;
            }

            if (redis.deleteNormalRank()) {
                ConfigEntity entity = new ConfigEntity();
                entity.setId(ConfigEntity.ID_RANK_RESET);
                entity.setValue(LocalDateTimeUtils.mysqlDatetimeToString(date));
                configCachedService.saveEntity(entity);
                logger.info("reset rank success: {}", Redis.KEY_NORMAL_RANK);
            } else {
                logger.warn("reset rank failed: {} becase redis delete failed.", Redis.KEY_NORMAL_RANK);
            }
        } catch (Exception e) {
            logger.warn(" {} exception: {}", Redis.KEY_NORMAL_RANK, e.toString());
            e.printStackTrace();
        }
    }

    private void saveTags() {
        try {
            updateUser();
            updateUserMission();
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
        }
    }

    private void updateUser() {
        long total = redis.getUserTagCount();
        if (total < 0) {
            logger.warn("getUserTagCount error.");
            return;
        }
        //logger.info("getUserTagCount total: {}", total);
        if (total == 0)
            return;

        long cnt = 0;
        Cursor<Map.Entry<Object, Object>> cursor = null;
        try {
            cursor = redis.getRedisTemplate().opsForHash()
                    .scan(Redis.KEY_USER_TAG, ScanOptions.scanOptions().count(1000).build());
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> it = cursor.next();
                String uidStr = (String)it.getKey();
                String tm = (String)it.getValue();
                ++cnt;
                int userId = Integer.valueOf(uidStr);
                saveUserTagOne(userId);
                // 防止无限循环，剩余的tag留给下次处理
                if (cnt >= total) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                logger.warn("exception: {}", e.toString());
            }
        }
    }

    private void updateUserMission() {
        long total = redis.getUserMissionTagCount();
        if (total < 0) {
            logger.warn("getUserMissionTagCount error.");
            return;
        }
        //logger.info("getUserMissionTagCount total: {}", total);
        if (total == 0)
            return;

        long cnt = 0;
        Cursor<Map.Entry<Object, Object>> cursor = null;
        try {
            cursor = redis.getRedisTemplate().opsForHash()
                    .scan(Redis.KEY_USER_MISSION_TAG, ScanOptions.scanOptions().count(1000).build());
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> it = cursor.next();
                String uidStr = (String)it.getKey();
                String tm = (String)it.getValue();
                ++cnt;
                int userId = Integer.valueOf(uidStr);
                saveUserMissionTagOne(userId);
                // 防止无限循环，剩余的tag留给下次处理
                if (cnt >= total) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("exception: {}", e.toString());
        } finally {
            try {
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                logger.warn("exception: {}", e.toString());
            }
        }
    }

    private boolean saveUserTagOne(int userId) {
        UserEntity userEntity = redis.getUser(userId);
        if (userEntity == null) {
            logger.warn("get user error. userId: {}", userId);
            return false;
        }

        // TODO use lua
        userCachedService.syncSaveEntity(userEntity);
        redis.removeUserTag(userId);

        logger.info("update user tag success. userId: {}", userId);
        return true;
    }

    private boolean saveUserMissionTagOne(int userId) {
        List<UserMissionEntity> userMissionEntityList = redis.getUserMission(userId);
        if (userMissionEntityList == null) {
            logger.warn("get user mission error. userId: {}", userId);
            return false;
        }

        // TODO use lua
        userMissionCachedService.syncSaveUserMissions(userId, userMissionEntityList);
        redis.removeUserMissionTag(userId);

        logger.info("update user mission tag success. userId: {}", userId);
        return true;
    }
}
