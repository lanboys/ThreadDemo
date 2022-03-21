package com.bing.lan.thread.lock.readWrite;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁测试，跟mysql中的 x锁(排它锁，写锁) 和 s锁(共享锁、读锁) 类似
 *
 * 廖雪峰
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581002092578
 */

public class ReentrantReadWriteLockTest {

  public static void main(String[] args) {

  }

  public class Counter {

    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();
    private int[] counts = new int[10];

    public void inc(int index) {
      wlock.lock(); // 加写锁
      try {
        counts[index] += 1;
      } finally {
        wlock.unlock(); // 释放写锁
      }
    }

    public int[] get() {
      rlock.lock(); // 加读锁
      try {
        return Arrays.copyOf(counts, counts.length);
      } finally {
        rlock.unlock(); // 释放读锁
      }
    }
  }
}
