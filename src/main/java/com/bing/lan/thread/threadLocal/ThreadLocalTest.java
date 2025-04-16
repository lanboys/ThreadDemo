package com.bing.lan.thread.threadLocal;

/**
 * Created by oopcoder at 2024/12/17 17:11 .
 */

public class ThreadLocalTest {

    public static ThreadLocal<String> name = new ThreadLocal<>();
    public static ThreadLocal<Integer> age = new ThreadLocal<>();

    public static void main(String[] args) {

        name.set("aa");

        age.set(5);

    }

}
