package com.bing.lan.thread.thread;

import java.util.concurrent.*;

/**
 * Created by oopcoder at 2024/12/12 15:51 .
 */

public class NewThreadTest {

    public static void main(String[] args) {
        callableTest();
    }

    private static void callableTest() {
        // 使用场景：需要返回值
        FutureTask<Object> task1 = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                for (int i = 0; i < 3; i++) {
                    System.out.println(Thread.currentThread().getName() + " 任务执行中 " + i);
                    Thread.sleep(1000);
                }
                return "ok";
            }
        });

        // 使用场景：不需要返回值，但是又想要在别的线程执行完成后再做其他操作, 还可以使用信号量等工具实现
        FutureTask<Object> task2 = new FutureTask<>(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + " 任务执行中 " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "默认返回值");

        Thread thread = new Thread(task2);
        thread.start();

        try {
            System.out.println(Thread.currentThread().getName() + " 正在等待子线程返回值...");
            System.out.println(Thread.currentThread().getName() + " 子线程返回值：" + task2.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + " 结束运行");

    }

}
