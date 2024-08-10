package com.bing.lan.thread;

/**
 * Created by oopcoder at 2022/8/22 21:54 .
 */

import java.util.concurrent.*;

public class DaemonThreadTest {

  /**
   * 可以尝试 分别运行 testNormalThread(); 和 testCompletableFuture(); 来感受区别
   */
  public static void main(String[] args) {

    testNormalThread();
    System.out.println("我是Main线程，我结束后，不影响线程的运行：" + Thread.currentThread().getName());

    // testCompletableFuture();
  }

  /**
   * 自己使用 Thread 方式模拟 守护线程和非守护线程
   */
  public static void testNormalThread() {
    Thread daemonThread = new Thread(() -> {
      System.out.println("我是守护线程：" + Thread.currentThread().getName());

      try {
        int count = 0;
        while (true) {
          Thread.sleep(5000L);
          System.out.println("======我是守护线程====" + count++);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    // 将线程设置为守护线程，当所有用户线程都死了，虚拟机就会退出，守护线程就会自动死了，看setDaemon()注释
    daemonThread.setDaemon(true);
    daemonThread.start();

    Thread thread = new Thread(() -> {
      System.out.println("我是子线程,Main线程结束后，我依然运行：" + Thread.currentThread().getName());

      try {

        for (int i = 0; i < 15; i++) {
          Thread.sleep(1000L);
          System.out.println("======执行操作 A====" + i);
        }
        System.out.println("我是子线程,即将结束：" + Thread.currentThread().getName());

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    // 将线程设置为非守护线程
    thread.setDaemon(false);
    thread.start();
  }

  /**
   * CompletableFuture 使用 voidCompletableFuture.get() 和不使用 voidCompletableFuture.get() 的区别
   */
  public static void testCompletableFuture() {
    CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
      System.out.println("我是子线程,Main线程结束后，我依然运行：" + Thread.currentThread().getName());

      try {
        Thread.sleep(1000L);
        System.out.println("======执行操作 A====");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).whenCompleteAsync((r, e) -> {
      System.out.println("======执行操作 B====");
    });
    try {
      // 该方法会阻塞Main线程，告诉Main线程，我执行完了，你才可以结束
      voidCompletableFuture.get();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}