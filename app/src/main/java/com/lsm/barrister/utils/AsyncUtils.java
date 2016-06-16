package com.lsm.barrister.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncUtils {

    //获取当前的cpu核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    //核心池大小
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    //最大限制
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    //保持
    private static final int KEEP_ALIVE = 1;

    /**
     * 线程工厂
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    /**
     * 静态阻塞队列
     */
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);

    /**
     * 线程池
     */
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE
            , MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
}
