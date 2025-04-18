package com.bing.lan.thread.utils;

public class LogUtil {

    public static final LogUtil out = new LogUtil();

    private String currentThreadName() {
        return String.format("[%s]", Thread.currentThread().getName());
    }

    public void println() {
        System.out.println(currentThreadName());
    }

    public void println(String x) {
        System.out.printf("%s %s\n", currentThreadName(), x);
    }

    public void printf(String format, Object... args) {
        Object[] args2 = new Object[args.length + 1];
        args2[0] = currentThreadName();
        System.arraycopy(args, 0, args2, 1, args.length);
        System.out.printf("%s " + format + "\n", args2);
    }
}
