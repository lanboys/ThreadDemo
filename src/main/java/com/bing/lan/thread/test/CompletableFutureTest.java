package com.bing.lan.thread.test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by lb on 2022/3/20.
 */

public class CompletableFutureTest {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    //test1();
    CompletableFuture<String> f0 = CompletableFuture.supplyAsync(
        () -> {
          print("11111");
          return "Hello World";
        });    //①

    CompletableFuture<String> f1 = f0.thenApply(s -> {
      String s1 = s + " QQ";
      print(s1+" [[[[[[[[");
      return s1;
    });  //②

    CompletableFuture<String> f2 = f1.thenApplyAsync(new Function<String, String>() {
      @Override
      public String apply(String s) {

        print(s);
        return s.toUpperCase();
      }
    });//③
    print("s");

    f2.get();
    f2.join();

  }

  private static void test1() throws InterruptedException, ExecutionException {
    //任务1：洗水壶->烧开水
    CompletableFuture<Void> f1 =
        CompletableFuture.runAsync(() -> {

          print("T1:洗水壶...");
          sleep(1, TimeUnit.SECONDS);
          print("T1:烧开水...");
          sleep(15, TimeUnit.SECONDS);
        });

    Void unused = f1.get();

    //任务2：洗茶壶->洗茶杯->拿茶叶
    CompletableFuture<String> f2 =
        CompletableFuture.supplyAsync(() -> {
          print("T2:洗茶壶...");
          sleep(1, TimeUnit.SECONDS);

          print("T2:洗茶杯...");
          sleep(2, TimeUnit.SECONDS);

          print("T2:拿茶叶...");
          sleep(1, TimeUnit.SECONDS);
          return "龙井";
        });

    //任务3：任务1和任务2完成后执行：泡茶
    CompletableFuture<String> f3 =
        f1.thenCombine(f2, (__, tf) -> {
          print("T1:拿到茶叶:" + tf);
          print("T1:泡茶...");
          return "上茶:" + tf;
        });
    //等待任务3执行结果
    print(f3.join());
  }

  public static void print(Object s) {
    System.out.println(Thread.currentThread().getName() + " " + s);
  }

  static void sleep(int t, TimeUnit u) {
    try {
      u.sleep(t);
    } catch (InterruptedException e) {
    }
  }
}
