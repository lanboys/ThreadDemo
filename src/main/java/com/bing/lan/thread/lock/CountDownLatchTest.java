package com.bing.lan.thread.lock;

import java.util.concurrent.*;

/**
 * Created by oopcoder at 2024/8/9 18:08 .
 * <p>
 * 闭锁
 */

public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        test1();
        // test2();
    }

    private static void test1() throws InterruptedException {
        int N = 4;
        CountDownLatch doneSignal = new CountDownLatch(N);
        for (int i = 0; i < N; ++i) {
            new Thread(new WorkerRunnable1(doneSignal, i)).start();
        }

        try {
            // 等待线程启动好
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("will await");
        doneSignal.await();
        System.out.println("end");
    }

    private static void test2() throws InterruptedException {
        int N = 4;
        CountDownLatch startSignal = new CountDownLatch(1);
        for (int i = 0; i < N; ++i) {
            new Thread(new WorkerRunnable2(startSignal, i)).start();
        }

        try {
            // 等待线程启动好
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("will countDown");
        startSignal.countDown();
        System.out.println("end");
    }

    static class WorkerRunnable1 implements Runnable {

        private final CountDownLatch doneSignal;
        private final int i;

        WorkerRunnable1(CountDownLatch doneSignal, int i) {
            this.doneSignal = doneSignal;
            this.i = i;
        }

        public void run() {
            doWork(i);
            doneSignal.countDown();
            System.out.println(Thread.currentThread().getName() + " work end ");

        }

        void doWork(int i) {

            for (int j = 0; j < i * 2 + 2; j++) {
                System.out.println(Thread.currentThread().getName() + " doWork ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class WorkerRunnable2 implements Runnable {

        private final CountDownLatch startSignal;
        private final int i;

        WorkerRunnable2(CountDownLatch startSignal, int i) {
            this.startSignal = startSignal;
            this.i = i;
        }

        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " await start ");
                startSignal.await();
                doWork(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        void doWork(int i) {

            for (int j = 0; j < i * 2; j++) {
                System.out.println(Thread.currentThread().getName() + " doWork ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
