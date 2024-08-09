package com.bing.lan.thread.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 这里是线程不安全的锁，只是随意写的，方便理解
 */

public class MyAQSLock extends AbstractQueuedSynchronizer implements Lock {

    // 线程不安全的状态，仅供理解
    private int myState = 0;

    /**
     * 真正处理加锁逻辑的地方，交给子类进行处理，父类处理好了队列逻辑
     */
    protected boolean tryAcquire(int acquires) {
        if (myState == 0) {
            // 获取锁成功
            myState = 1;
            return true;
        }
        // 获取锁失败
        return false;
    }

    protected final boolean tryRelease(int releases) {
        if (myState == 1) {
            // 释放锁成功
            myState = 0;
            return true;
        }
        // 释放锁失败
        return false;
    }

    @Override
    public void lock() {
        // 阻塞获取，一定要获取到
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
        // 非阻塞获取，获取不到立马返回给用户 由用户来决定接下来干嘛，就像上面的获取锁 acquire() 也调用了这个方法，获取不到锁就进队列
        return tryAcquire(1);
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
