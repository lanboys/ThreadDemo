package com.bing.lan.thread.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by backend.
 */

public class MyAQSLock extends AbstractQueuedSynchronizer implements Lock {

  /**
   * 真正处理加锁逻辑的地方，交给子类进行处理，父类处理好了队列逻辑
   */
  protected boolean tryAcquire(int acquires) {
    int c = getState();
    if (c == 0) {
      //公平锁
      //如果当前线程前面有一个排队的线程，则为True;
      //如果当前线程位于队列的头部或队列为空，则为false
      boolean predecessors = hasQueuedPredecessors();
      if (!predecessors && compareAndSetState(0, acquires)) {
        return true;
      }
    }
    return false;
  }

  protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    boolean free = false;
    if (c == 0) {
      free = true;
    }
    setState(c);
    return free;
  }

  @Override
  public void lock() {
    acquire(1);
  }

  @Override
  public void unlock() {
    release(1);
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {

  }

  @Override
  public boolean tryLock() {
    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public Condition newCondition() {
    return new ConditionObject();
  }
}
