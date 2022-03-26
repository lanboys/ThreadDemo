package com.bing.lan.thread.aqs;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AQSConditionTest {

  //public static MyAQSLock lock = new MyAQSLock();
  public static ReentrantLock lock = new ReentrantLock(true);
  public static Condition notFull = lock.newCondition();

  public volatile static boolean awaitFlag = true;

  public static void main(String[] args) throws IOException {
    while (true) {
      int read = System.in.read();
      if (read == 49) {//1
        newAwaitThread(2);
      } else if (read == 50) {//2
        awaitFlag = !awaitFlag;
        System.out.println("set await flag : " + awaitFlag);
      } else if (read == 51) {//3
        newSignalThread(2, false);
      } else if (read == 52) {//4
        newSignalThread(2, true);
      } else if (read == 53) {//5
        newThread(10);
      }
    }
  }

  public static void newAwaitThread(long s) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("await " + Thread.currentThread().getName() + " try lock...");
        lock.lock();
        System.out.println("await " + Thread.currentThread().getName() + " lock ok...");
        try {
          while (awaitFlag) {
            System.out.println("await " + Thread.currentThread().getName() + " will await and release lock...");
            notFull.await();
            System.out.println("await " + Thread.currentThread().getName() + " await end and reLock ok...");
          }
          Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
          System.out.println("await " + Thread.currentThread().getName() + " unlock ok...");
        }
      }
    });

    thread.start();
  }

  public static void newSignalThread(long s, boolean signalAll) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("signal " + Thread.currentThread().getName() + " try lock...");
        lock.lock();
        System.out.println("signal " + Thread.currentThread().getName() + " lock ok...");
        try {
          if (signalAll) {
            System.out.println("signal " + Thread.currentThread().getName() + " will signalAll...");
            notFull.signalAll();
            System.out.println("signal " + Thread.currentThread().getName() + " signalAll end...");
          } else {
            System.out.println("signal " + Thread.currentThread().getName() + " will signal...");
            notFull.signal();
            System.out.println("signal " + Thread.currentThread().getName() + " signal end...");
          }
          Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
          System.out.println("signal " + Thread.currentThread().getName() + " unlock ok...");
        }
      }
    });

    thread.start();
  }

  public static void newThread(long s) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " try lock...");
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " lock ok...");
        try {
          Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
          System.out.println(Thread.currentThread().getName() + " unlock ok...");
        }
      }
    });

    thread.start();
  }
}
