package com.study.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：Tangren on 2019-07-26
 * 包名：com.study.util
 * 邮箱：996489865@qq.com
 * TODO:简单线程管理
 */
public class ThreadPoolManage {
    private static int coreSize = Runtime.getRuntime().availableProcessors() * 2;
    private static final int maxSize = 15;
    private static final long keepAliveTime = 1L;
    private ThreadPoolExecutor executor;
    private AtomicInteger runnableCount = new AtomicInteger(0);
    private volatile static ThreadPoolManage instance = null;

    private ThreadPoolManage() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(30);
        ThreadFactory factory = r -> {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.NORM_PRIORITY);
            thread.setName("N_" + runnableCount.getAndIncrement() + thread.getName());
            return thread;
        };
        if (coreSize > maxSize) {
            coreSize = maxSize - 1;
        }
        executor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS, queue, factory, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static ThreadPoolManage getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolManage.class) {
                if (instance == null) {
                    instance = new ThreadPoolManage();
                }
            }
        }
        return instance;
    }

    /**
     * 提交普通任务
     *
     * @param runnable 任务
     */
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * 提交有返回的普通任务
     *
     * @param runnable 任务
     * @return .
     */
    public Future<?> submit(Runnable runnable) {
        return executor.submit(runnable);
    }

    /**
     * 关闭普通任务线程池
     */
    public void shutDownNormal() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /**
     * 关闭所有任务
     */
    public void showDownAll() {
        shutDownNormal();
    }
}
