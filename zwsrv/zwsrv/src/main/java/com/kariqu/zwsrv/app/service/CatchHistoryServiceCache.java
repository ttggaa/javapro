package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.cache.RedisExpireCatchHistoryCache;
import com.kariqu.zwsrv.app.model.CatchHistoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Service
public class CatchHistoryServiceCache {

    public static final int kMaxCatchHistory = 16;

    BlockingQueue<CatchHistoryInfo> blockingQueue = new LinkedBlockingDeque<>(1000);
    WorkerThread thread;

    @Autowired
    RedisExpireCatchHistoryCache redisExpireCatchHistoryCache;

    public CatchHistoryServiceCache() {
        this.thread = new WorkerThread(blockingQueue);
        this.thread.start();
    }

    public void leftPush(CatchHistoryInfo CatchHistoryInfo) {
        blockingQueue.add(CatchHistoryInfo);
    }

    public List<CatchHistoryInfo> range(long start, long end) {
        return redisExpireCatchHistoryCache.range(start,end);
    }

    public class WorkerThread extends Thread {

        private volatile boolean isRunning = true;
        private BlockingQueue<CatchHistoryInfo> queue;

        public WorkerThread(BlockingQueue<CatchHistoryInfo> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            super.run();
            try {
                while (isRunning) {
                    CatchHistoryInfo record = queue.poll(5000, TimeUnit.MILLISECONDS);
                    if (record !=null) {
                        long num = redisExpireCatchHistoryCache.leftPush(record);
                        while (num> kMaxCatchHistory) {
                            redisExpireCatchHistoryCache.rightPop();
                            num--;
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
