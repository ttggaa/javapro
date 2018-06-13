package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.cache.RedisExpireGameRewardCache;
import com.kariqu.zwsrv.app.cache.RedisExpireRoomRewardCache;
import com.kariqu.zwsrv.app.model.GameRewardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by simon on 08/01/2018.
 */
@Service
public class GameRewardService {

    public static final int kGameRewardMaxCacheNum = 16;
    public static final int kRoomRewardMaxCacheNum = 10;

    BlockingQueue<GameRewardInfo> blockingQueue = new LinkedBlockingDeque<>(1000);
    WorkerThread thread;

    @Autowired
    RedisExpireGameRewardCache redisExpireGameRewardCache;

    @Autowired
    RedisExpireRoomRewardCache redisExpireRoomRewardCache;

    public GameRewardService() {
        this.thread = new WorkerThread(blockingQueue);
        this.thread.start();
    }

    public void leftPush(GameRewardInfo gameRewardInfo) {
        blockingQueue.add(gameRewardInfo);
    }

    public List<GameRewardInfo> range(long start, long end) {
        return redisExpireGameRewardCache.range(start,end);
    }

    public List<GameRewardInfo> range(int roomId, long start, long end) {
        return redisExpireRoomRewardCache.range(roomId,start,end);
    }

    public class WorkerThread extends Thread {

        private volatile boolean isRunning = true;
        private BlockingQueue<GameRewardInfo> queue;

        public WorkerThread(BlockingQueue<GameRewardInfo> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            super.run();
            try {
                while (isRunning) {
                    GameRewardInfo item = queue.poll(5000, TimeUnit.MILLISECONDS);
                    if (item !=null) {
                        long roomRewardNum = redisExpireRoomRewardCache.leftPush(item.getRoomId(),item);
                        while (roomRewardNum>kRoomRewardMaxCacheNum) {
                            redisExpireRoomRewardCache.rightPop(item.getRoomId());
                            roomRewardNum--;
                        }

                        long gameRewardNum = redisExpireGameRewardCache.leftPush(item);
                        while (gameRewardNum>kGameRewardMaxCacheNum) {
                            redisExpireGameRewardCache.rightPop();
                            gameRewardNum--;
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
