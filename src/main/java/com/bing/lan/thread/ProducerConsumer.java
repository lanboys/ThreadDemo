package com.bing.lan.thread;

import java.util.Arrays;

/**
 * 生产者消费者
 */
public class ProducerConsumer {

    public static void main(String[] args) {
        SyncStack ss = new SyncStack();
        new Thread(new Producer(ss, "p1")).start();
        new Thread(new Producer(ss, "p2")).start();
        new Thread(new Consumer(ss, "c1")).start();
    }

    static class WoTou {

        int id;
        String pName;

        WoTou(int id, String pName) {
            this.id = id;
            this.pName = pName;
        }

        public String toString() {
            return "WoTou : " + pName + "-" + id;
        }
    }

    static class SyncStack {

        int index = 0;
        WoTou[] arrWT = new WoTou[2];

        public void push(WoTou wt) {
            synchronized (ProducerConsumer.class) {
                while (index == arrWT.length) {
                    try {
                        ProducerConsumer.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ProducerConsumer.class.notifyAll();
                arrWT[index] = wt;
                index++;
            }
        }

        public WoTou pop() {
            synchronized (ProducerConsumer.class) {
                while (index == 0) {
                    try {
                        ProducerConsumer.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ProducerConsumer.class.notify();
                index--;
                return arrWT[index];
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
        String name;

        Producer(SyncStack ss, String name) {
            this.ss = ss;
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 30; i++) {
                WoTou wt = new WoTou(i, name);
                ss.push(wt);
                System.out.println("生产了：" + wt);
                try {
                    Thread.sleep((int) (Math.random() * 200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {

        SyncStack ss = null;
        String name;

        Consumer(SyncStack ss, String name) {
            this.ss = ss;
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 60; i++) {
                WoTou wt = ss.pop();
                System.out.println("                                  消费了: " + wt);
                try {
                    Thread.sleep((int) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("run(): " + ss);
        }
    }
}