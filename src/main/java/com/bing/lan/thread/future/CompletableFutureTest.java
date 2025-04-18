package com.bing.lan.thread.future;

import com.bing.lan.thread.utils.LogUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by oopcoder at 2025/4/16 22:43 .
 */

public class CompletableFutureTest {

    private static final int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static final int maxPoolSize = corePoolSize * 2;
    private static final int queueCapacity = 500; // 队列容量限制

    private static CompletableFuture<Void> allTasksFuture;
    private static AtomicLong completedEmbedding = new AtomicLong(0);
    private static AtomicLong completedTasks = new AtomicLong(0);
    private static AtomicLong failedTasks = new AtomicLong(0);
    private static AtomicReference<Exception> criticalException = new AtomicReference<>(null);
    private static volatile long finishFlag = 0;

    // 自定义拒绝策略：当队列满时，由提交线程自己执行任务
    static RejectedExecutionHandler callerRunsPolicy = (runnable, executor) -> {
        LogUtil.out.printf("线程池队列已满(当前大小:{%s}), 任务将由主线程直接执行", executor.getQueue().size());

        runnable.run();
    };

    private static ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueCapacity), callerRunsPolicy);

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        allTasksFuture = new CompletableFuture<>();

        int total = 10;
        finishFlag = total;// 必须有，不然直接就结束了

        finishFlag = submitTask(total);

        checkAllTaskCompleted();

        // 等待所有任务完成
        allTasksFuture.get();

        LogUtil.out.println("所有任务都结束了");
        // 关闭线程池
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static int submitTask(int total) {
        int finish = 0;
        for (int i = 0; i < total; i++) {
            if (i == 3) {
                LogUtil.out.println(i + " 抛异常了");
                failedTasks.incrementAndGet();
            } else {
                LogUtil.out.println(i + " 提交任务成功");
                int finalI = i;
                CompletableFuture.runAsync(() -> processDocument(finalI), executorService);
                finish++;
            }
        }
        return finish;
    }

    private static void processDocument(int index) {
        try {
            for (int i = 0; i < 5; i++) {
                LogUtil.out.println(index + "======执行操作 A====" + Thread.currentThread().getName());
                Thread.sleep(1000L);
            }
            LogUtil.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            completedTasks.incrementAndGet();
        }
        checkAllTaskCompleted();
    }


    public static void checkAllTaskCompleted() {
        // 检查是否所有任务都已完成, 完成后主线程继续执行
        LogUtil.out.println("检查所有任务是否结束: " + finishFlag);
        if (completedTasks.get() == finishFlag) {
            allTasksFuture.complete(null);
            LogUtil.out.println("所有任务结束");
        }
    }

}
