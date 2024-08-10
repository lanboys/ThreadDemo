package com.bing.lan.thread.test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import sun.misc.Unsafe;

public class ContainerTest {

  private static Unsafe unsafe;
  private static long valueOffset;

  private int value;

  public static void main(String[] args) throws NoSuchFieldException {

    ExecutorService executorService = getExecutor();

    for (int i = 0; i < 1; i++) {
      int finalI = i;

      FutureTask task = new FutureTask<String>(() -> 1 + 2 + "");

      new Thread(task).start();

      printResult(task);


      executorService.execute(new Runnable() {
        @Override
        public void run() {
          print(finalI);
        }
      });

      Future<?> future = executorService.submit(new Runnable() {
        @Override
        public void run() {
          print(finalI);
        }
      });
      printResult(future);

      Future<String> result = executorService.submit(new Runnable() {
        @Override
        public void run() {
          print(finalI);
        }
      }, "result");

      printResult(result);
    }
  }

  private static void printResult(Future result) {
    Object s = null;
    try {
      s = result.get();
      print(s);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

  public static ExecutorService getExecutor() {
    return Executors.newSingleThreadExecutor();
  }

  public static void print(Object s) {
    System.out.println(Thread.currentThread().getName() + " " + s);
  }

  public static void main1(String[] args) throws NoSuchFieldException {

    // AtomicReference atomicReference = new AtomicReference(new Object());
    //
    // Object o = atomicReference.get();

    ReferenceTest test = new ReferenceTest();
    test.setX(1);
    test.setY(2);

    int stamp = test.getStamp();
    AtomicStampedReference<ReferenceTest> atomicStampedReference = new AtomicStampedReference<>(test, stamp);

    ReferenceTest test1 = new ReferenceTest();
    test1.setX(11);
    test1.setY(21);

    int stamp1 = test.getStamp();

    test.setY(4);

    boolean b = atomicStampedReference.compareAndSet(test, test1, stamp, stamp1);

    System.out.println("main(): " + b);
  }

  private static void foo1(Object next) {
    Object[] objects = new Object[3];

    CopyOnWriteArrayList copy = new CopyOnWriteArrayList(new ArrayList());

    boolean add = copy.add(new Object());

    Iterator iterator = copy.iterator();
    iterator.hasNext();
  }

  private static void foo2(Object next) {
    ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

    ConcurrentSkipListMap<String, Object> map1 = new ConcurrentSkipListMap<>();

    HashMap<Object, Object> map2 = new HashMap<>();

    Object put = map2.put("2", "4");

    Object put1 = map.put("1", "2");
  }
}
