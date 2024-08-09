package com.bing.lan.thread;

import java.util.*;

/**
 * 生产者消费者
 */
public class ProducerConsumerV2 {

    public static int PRODUCER_WT_COUNT = 10;
    public static int CONSUMER_WT_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        SyncStack ss = new SyncStack();

        // 如果修改了线程，要注意数量修改

        Thread p1 = new Thread(new Producer(ss, PRODUCER_WT_COUNT), "p1");
        Thread p2 = new Thread(new Producer(ss, PRODUCER_WT_COUNT), "p2");
        // =================
        Thread c1 = new Thread(new Consumer(ss, CONSUMER_WT_COUNT), "c1");
        Thread c2 = new Thread(new Consumer(ss, CONSUMER_WT_COUNT), "c2");

        p1.start();
        p2.start();
        c1.start();
        c2.start();

        p1.join();
        p2.join();
        c1.join();
        c2.join();

        System.out.println("程序结束");
    }

    static class WoTou {

        int id;
        String pName;

        WoTou(int id, String pName) {
            this.id = id;
            this.pName = pName;
        }

        public String toString() {
            return "WoTou[" + pName + "-" + id + "]";
        }
    }

    static class SyncStack {

        int index = 0;
        WoTou[] arrWT = new WoTou[2];

        public void push(WoTou wt) {
            synchronized (ProducerConsumer.class) {
                System.out.println(Thread.currentThread().getName() + " 获取到锁，准备生产");

                while (index == arrWT.length) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 容器满了，生产失败，准备休眠，并释放锁");
                        ProducerConsumer.class.wait();
                        // 继续执行的时候，表示休眠结束并且重新获取到锁了，
                        // 重点是：被人唤醒到重新获取锁是有一段时间差的，所以此刻可能并不是结束休眠的时间
                        System.out.println(Thread.currentThread().getName() + " 休眠结束，并重新获取到锁了，继续执行");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                arrWT[index] = wt;
                index++;
                System.out.println(Thread.currentThread().getName() + " 生产了：" + wt);

                // 这里最好的做法是唤醒所有     消费者线程，但是做不到，需要用Condition
                // 不然如果容器满了，下一个获取到锁的又是生产者，那生产者就得休眠，浪费了一段时间
                ProducerConsumer.class.notifyAll();
                System.out.println(Thread.currentThread().getName() + " 生产完成，唤醒所有休眠线程，去获取锁");
                System.out.println(Thread.currentThread().getName() + " 释放锁，准备下一次生产");
            }
        }

        public WoTou pop() {
            synchronized (ProducerConsumer.class) {
                System.out.println("                                                   " + Thread.currentThread().getName() + " 获取到锁，准备消费");

                while (index == 0) {
                    try {
                        System.out.println("                                                   " + Thread.currentThread().getName() + " 容器空了，消费失败，准备休眠，并释放锁");
                        ProducerConsumer.class.wait();
                        System.out.println("                                                   " + Thread.currentThread().getName() + " 休眠结束，并重新获取到锁了，继续执行");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                index--;
                WoTou wt = arrWT[index];
                System.out.println("                                                   " + Thread.currentThread().getName() +
                        " 消费了: " + wt);

                // 这里最好的做法是唤醒所有     生产者线程，但是做不到，需要用Condition
                ProducerConsumer.class.notifyAll();
                // ProducerConsumer.class.notify();
                System.out.println("                                                   " + Thread.currentThread().getName() + " 消费成功，唤醒所有休眠线程，去获取锁");
                System.out.println("                                                   " + Thread.currentThread().getName() + " 释放锁，准备下一次消费");
                return wt;
            }
        }

        @Override
        public String toString() {
            return "SyncStack{" +
                    "index=" + index +
                    ", arrWT=" + Arrays.toString(arrWT) +
                    '}';
        }
    }

    static class Producer implements Runnable {

        SyncStack ss = null;
        int count;

        Producer(SyncStack ss, int count) {
            this.ss = ss;
            this.count = count;
        }

        public void run() {
            for (int i = 0; i < count; i++) {

                try {
                    Thread.sleep((int) (Math.random() * 200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                WoTou wt = new WoTou(i, Thread.currentThread().getName());
                ss.push(wt);
            }
            System.out.println(Thread.currentThread().getName() + " 生产结束");
        }
    }

    static class Consumer implements Runnable {

        SyncStack ss = null;
        int count;

        Consumer(SyncStack ss, int count) {
            this.ss = ss;
            this.count = count;

        }

        public void run() {
            for (int i = 0; i < count; i++) {
                ss.pop();

                try {
                    Thread.sleep((int) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(Thread.currentThread().getName() + " 消费结束");

        }
    }
}