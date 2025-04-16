package com.bing.lan.thread.pool;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by oopcoder at 2024/8/9 22:06 .
 */

public class CommonThreadPoolTest {

    public static void main(String[] args) {
        // cachedThreadPool();
        // fixedThreadPool();
        scheduledThreadPoolTest();
    }

    private static void cachedThreadPool() {
        // 使用的是工作队列SynchronousQueue，这是一个不存储元素的队列，
        // 如果有空闲子线程在队列里面获取任务，可以通过队列将任务交给子线程执行，
        // 如果没有空闲子线程，将不会接收任务，直接创建新的子线程去执行。
        ExecutorService threadPool = Executors.newCachedThreadPool();

        for (int i = 1; i < 48; i++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threadPool.execute(new WorkerRunnable(i));
        }

        threadPool.shutdown();
        System.out.println("end");
    }

    private static void fixedThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        // ExecutorService threadPool = Executors.newSingleThreadExecutor();

        for (int i = 1; i < 18; i++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threadPool.execute(new WorkerRunnable(i));
        }

        threadPool.shutdown();
        System.out.println("end");
    }

    private static void scheduledThreadPoolTest() {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);

        for (int i = 1; i < 3; i++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(new Date() + "  " + Thread.currentThread().getName() + " execute work " + i);

            // 延时x秒后，开始执行一次，注意：不是说3秒后一定会执行，得看线程忙不忙，有没有空闲线程，所以时间不精确
            // threadPool.schedule(new WorkerRunnable(i), 3, TimeUnit.SECONDS);

            // 延时x秒后，开始执行第一次，每隔x秒再执行第二次，依次循环，
            threadPool.scheduleAtFixedRate(new WorkerRunnable(i), 1, 3, TimeUnit.SECONDS);

            // 延时x秒后，开始执行第一次，执行完成后，每隔x秒再执行第二次，依次循环，
            // threadPool.scheduleWithFixedDelay(new WorkerRunnable(i), 3, 15, TimeUnit.SECONDS);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threadPool.shutdown();
        System.out.println("end");
    }

    static class WorkerRunnable implements Runnable {

        private final int i;

        WorkerRunnable(int i) {
            this.i = i;
        }

        public void run() {
            System.out.println(new Date() + "  " + Thread.currentThread().getName() + " work start " + i);
            doWork(i);
            System.out.println(new Date() + "  " + Thread.currentThread().getName() + " work end " + i);
        }

        void doWork(int i) {
            int max = i * 2 + 2;
            if (max > 10) {
                max = max % 5;
            }
            for (int j = 0; j < max; j++) {
                System.out.println(new Date() + "  " + Thread.currentThread().getName() + " doWork " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
