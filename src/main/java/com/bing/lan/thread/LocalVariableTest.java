package com.bing.lan.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为什么局部变量是线程安全的？
 * https://time.geekbang.org/column/article/86695
 *
 * 我认为 局部变量也不一定是安全的，下面就不安全
 */

public class LocalVariableTest {

  public static void main(String[] args) throws InterruptedException {
    new LocalVariableTest().local();
  }

  public void local() throws InterruptedException {
    final int[] pos = {0};
    AtomicInteger a = new AtomicInteger();

    Thread T1 = new Thread(() -> {
      for (int i = 0; i < 10000000; i++) {
        pos[0] = pos[0] + 1;
      }
      System.out.println("pos2: " + pos[0]);
      for (int i = 0; i < 10000000; i++) {
        a.getAndIncrement();
      }
      System.out.println("a2: " + a);
    });

    T1.start();
    Thread.sleep(1);

    for (int i = 0; i < 10000000; i++) {
      pos[0] = pos[0] + 1;
    }
    for (int i = 0; i < 10000000; i++) {
      a.getAndIncrement();
    }

    System.out.println("pos1: " + pos[0]);
    System.out.println("a1: " + a);
    T1.join();
    System.out.println("pos3: " + pos[0]);
    System.out.println("a3: " + a);
  }
}
