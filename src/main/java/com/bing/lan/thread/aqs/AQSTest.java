package com.bing.lan.thread.aqs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://www.cnblogs.com/wang-meng/p/12816829.html
 *
 * ReentrantLock中 boolean nonfairTryAcquire(int acquires) 方法为什么不放NonfairSync中，而是放在Sync中，
 * 因为公平锁的 tryLock() 方法也是尝试加非公平锁，如果去加公平锁的话，就会阻塞，就不是尝试加锁而是直接加锁了，
 * 详情可查看tryLock注释
 */

public class AQSTest {

  //public static MyAQSLock lock = new MyAQSLock();
  public static ReentrantLock lock = new ReentrantLock(true);

  public static List<Thread> threads = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    while (true) {
      int read = System.in.read();
      if (read == 49) {//1
        newThread(2);
      } else if (read == 50) {//2
        if (!threads.isEmpty()) {
          Thread thread = threads.remove(0);
          System.out.println(thread.getName() + " will unlock...");
        } else {
          System.out.println("no thread...");
        }
      }
    }
  }

  public static void newThread(long s) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " try lock...");
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " lock ok...");
        try {
          while (threads.contains(Thread.currentThread())) {
            Thread.sleep(s * 1000);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
          System.out.println(Thread.currentThread().getName() + " unlock ok...");
          threads.remove(Thread.currentThread());
        }
      }
    });
    threads.add(thread);

    thread.start();
  }
}
