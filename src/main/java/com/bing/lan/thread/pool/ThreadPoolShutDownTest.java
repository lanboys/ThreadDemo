package com.bing.lan.thread.pool;

import java.util.*;
import java.util.concurrent.*;

public class ThreadPoolShutDownTest {

    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(2, 4,
                10000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(20));

        for (int i = 1; i < 15; i++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            threadPool.execute(new WorkerRunnable(i));
        }

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // threadPool.shutdown();// 会完成已经提交的任务，不再接受新任务
        threadPool.shutdownNow();// 正在执行的任务会继续完成，但是队列里面的任务不会，会被返回
        // threadPool.execute(new WorkerRunnable(99));

        // try {
        //     if (!threadPool.awaitTermination(2, TimeUnit.SECONDS)) {
        //     }
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }

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

            boolean interrupted = false;

            for (int j = 0; j < 2; j++) {
                System.out.println(new Date() + "  " + Thread.currentThread().getName() + " doWork " + i);
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("doWork(): " + Thread.currentThread().isInterrupted());

                    // 此时线程的中断状态是 false
                    e.printStackTrace();
                    interrupted = true;
                }
            }

            if (interrupted) {
                Thread.currentThread().interrupt();
            }

            // 不过这里需要注意的是，中断线程并不代表线程立刻结束，只是通过工作线程的interrupt()实例方法设置了中断状态，
            // 这里 需要用户程序主动配合线程进行中断操作。
            // 下面这段代码执行的时候遇到了中断，就啥事也没干，仍然继续执行下去，但是线程的中断状态是true

            // int n = 1;
            // while (true) {
            //     if (n % 100000000 == 0) {
            //         System.out.println(new Date() + "  " + Thread.currentThread().getName() + " doWork " + i);
            //     }
            //     if (n++ > 999999999) {
            //         break;
            //     }
            // }
            // System.out.println("doWork(): " + Thread.currentThread().isInterrupted());

        }
    }

}
