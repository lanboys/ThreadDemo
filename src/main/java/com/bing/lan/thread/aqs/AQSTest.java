package com.bing.lan.thread.aqs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://javadoop.com/post/AbstractQueuedSynchronizer
 * https://www.cnblogs.com/wang-meng/p/12816829.html
 * <p>
 * 公平锁和非公平锁的区别不大，主要区别在调用 lock() 方法的时候是不是直接去插队加锁。
 * <p>
 * 非公平锁：lock() 一上来就插队加锁，不管队列里面的排队的线程，如果失败了，走 acquire()，tryAcquire()，nonfairTryAcquire()，
 * 再次判断锁有没有被获取，没有的话，再一次插队加锁，如果这次还加锁失败，就进队列
 * <p>
 * 公平锁：lock()，acquire()，tryAcquire()，如果锁没有被获取，并且队列也为空的话，这时候直接加锁，否则加锁失败，进队列
 *
 * <p>
 * <p> ====================================
 * <p>
 * ReentrantLock 中 boolean nonfairTryAcquire(int acquires) 方法为什么不放 NonfairSync 中，而是放在 Sync 中，
 * <p>
 * tryLock() 是希望此刻就加锁，不管公平、不公平，不管队列里面有没有等待线程，都要尝试立马加锁
 * <p>
 * 所以公平锁也是调用 nonfairTryAcquire()，如果去调用 tryAcquire() 话，就会检查队列是否有线程在排队，如果有线程排队，都不尝试，就直接返回获取不到锁，
 * 不符合 tryLock() 方法的定义。
 * <p>
 * <p>
 * 详情可查看tryLock注释
 */

public class AQSTest {

    // public static MyAQSLock lock = new MyAQSLock();
    public static ReentrantLock lock = new ReentrantLock(true);

    public static List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        while (true) {
            int read = System.in.read();
            if (read == 49) {// 1
                newThread(2);
            } else if (read == 50) {// 2
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
