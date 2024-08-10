package com.bing.lan.thread.pool;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Created by oopcoder at 2024/8/9 22:06 .
 */

public class ThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(0, 10,
                10000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new MyThreadFactory());

        for (int i = 0; i < 8; i++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            threadPool.execute(new WorkerRunnable(i));
        }

        try {
            Thread.sleep(10000);
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

    static class MyThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
