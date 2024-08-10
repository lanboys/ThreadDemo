package com.bing.lan.thread.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lb on 2022/3/20.
 */

public class ReferenceTest {

  int x;
  int y;

  public static AtomicInteger stamp = new AtomicInteger();

  public int getStamp() {
    return stamp.get();
  }

  public ReferenceTest() {
    stamp.getAndIncrement();
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
    stamp.getAndIncrement();
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
    stamp.getAndIncrement();
  }
}
