package com.kariqu.tyt.http.utility;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class SimpleEventDispatcher<EventType> {

    private BlockingQueue<EventType> blockingQueue = new LinkedBlockingDeque<>();
    private WorkerThread mWorkerThread;
    private long idleTimeout = 2500;

    protected SimpleEventDispatcher() {
        mWorkerThread = new WorkerThread(blockingQueue);
        mWorkerThread.start();
    }

    protected SimpleEventDispatcher(long idleTimeoutArg) {
        idleTimeout=idleTimeoutArg;
        mWorkerThread = new WorkerThread(blockingQueue);
        mWorkerThread.start();
    }

    public void put(EventType event) {
        if (event!=null) {
            blockingQueue.add(event);
        }
    }

    public void shutdown() {
        mWorkerThread.interrupt();
    }

    protected void dispatchInWorkThread(EventType event) {

    }

    protected void dispatchOnIdle() {

    }

    protected void dispatchOnExit() {

    }

    public class WorkerThread extends Thread  {

        private final BlockingQueue<EventType> queue;

        public WorkerThread(BlockingQueue<EventType> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            long timestamp = System.currentTimeMillis();
            while (!isInterrupted()) {
                try {
                    EventType event = queue.poll(idleTimeout, TimeUnit.MILLISECONDS);
                    if (event!=null) {
                        dispatchInWorkThread(event);
                        if (System.currentTimeMillis()-timestamp>=idleTimeout) {
                            dispatchOnIdle();
                            timestamp = System.currentTimeMillis();
                        }
                    } else {
                        dispatchOnIdle();
                        timestamp = System.currentTimeMillis();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
            }
            dispatchOnExit();
        }
    }
}

