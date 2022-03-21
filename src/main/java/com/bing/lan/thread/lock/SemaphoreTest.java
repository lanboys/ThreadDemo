package com.bing.lan.thread.lock;

import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * 信号量
 */
public class SemaphoreTest {

  public static void main(String[] args) throws IOException {
    Semaphore semaphore = new Semaphore(2);
    while (true) {
      int read = System.in.read();
      if (read == 49) {
        System.out.println("main new thread ...");

        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              System.out.println(Thread.currentThread().getName() + " acquire: ...");
              semaphore.acquire();
              System.out.println(Thread.currentThread().getName() + " will sleep: ...");
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } finally {
              System.out.println(Thread.currentThread().getName() + " release: ...");
              semaphore.release();
            }
          }
        }).start();
      }
    }
  }
}
