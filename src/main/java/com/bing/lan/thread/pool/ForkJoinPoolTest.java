package com.bing.lan.thread.pool;

import java.util.concurrent.*;

/**
 * Created by oopcoder at 2024/8/10 15:45 .
 *
 * https://www.cnblogs.com/FraserYu/p/14439497.html
 */

public class ForkJoinPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        for (int i = 0; i < 10; i++) {
            ForkJoinTask<Integer> task = forkJoinPool.submit(new Fibonacci(i));
            System.out.println(task.get());
        }
    }

    public static class Fibonacci extends RecursiveTask<Integer> {

        int n;

        public Fibonacci(int n) {
            this.n = n;
        }

        @Override
        public Integer compute() {
            if (n <= 1) {
                return n;
            }
            Fibonacci f1 = new Fibonacci(n - 1);
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            f2.fork();
            return f1.join() + f2.join();
        }
    }

}
