package com.bing.lan.thread.lock;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试 等待队列被唤醒后，从哪里开始执行
 */
public class ReentrantLockAwaitTest {

  public static void main(String[] args) throws IOException {
    ReentrantLockAwaitTest lockTest = new ReentrantLockAwaitTest();

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          System.out.println("run(): --------------");
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          lockTest.start();
          System.out.println("run(): ***************");
        }
      }
    }).start();

    while (true) {
      int read = System.in.read();
      if (read == 49) {
        lockTest.add();
      }
    }
  }

  ReentrantLock lock = new ReentrantLock(true);

  Condition notFull = lock.newCondition();

  int count = 0;

  public void start() {
    lock.lock();
    try {
      System.out.println("start(): start...");
      while (count % 5 != 0) {
        System.out.println(Thread.currentThread().getName() + " start(): await1...");
        //boolean l = notFull.await(8, TimeUnit.SECONDS);
        notFull.await();// 在notFull等待队列被唤醒后，开始获取锁，成功获取后是从这里之后开始执行的
        System.out.println(Thread.currentThread().getName() + " start(): await2...");
      }
      System.out.println("start(): end...");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void add() {
    System.out.println(Thread.currentThread().getName() + " add(): add1...");
    lock.lock();
    try {
      System.out.println(Thread.currentThread().getName() + " add(): add2...");
      count++;
      notFull.signal();
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } finally {
      lock.unlock();
    }
    System.out.println(Thread.currentThread().getName() + " add(): add3...");
  }
}
