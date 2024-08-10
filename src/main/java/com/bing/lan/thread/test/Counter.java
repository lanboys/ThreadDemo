package com.bing.lan.thread.test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lb on 2022/3/20.
 */

public class Counter extends AtomicInteger {

  public static void main(String[] args) throws IOException {
    Counter counter = new Counter();

    while (true) {
      int read = System.in.read();
      if (read == 49) {
        System.out.println("main new thread ...");
        new Thread(new Runnable() {
          @Override
          public void run() {
            System.out.println(Thread.currentThread().getName() + " before count: " + counter.get());
            counter.incrementAndGet1();
            counter.incrementAndGet();
            System.out.println(Thread.currentThread().getName() + " after count: " + counter.get());
          }
        }).start();
      }
    }
  }

  public int incrementAndGet1() {
    int prev, next;
    do {
      prev = get();
      next = prev + 1;
    } while (!compareAndSet(prev, next));
    return next;
  }
}
