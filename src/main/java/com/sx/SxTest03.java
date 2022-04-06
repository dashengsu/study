package com.sx;

/**
 * 交替输出问题:使用synchronized关键字进行
 */
public class SxTest03 {
    private static final Object count = new Object();
    public static void main(String[] args) {
        method01();
    }
    private static void method01(){
        final char[] chars = "123456789".toCharArray();
        final char[] chars1 = "ABCDEFGHI".toCharArray();
        new Thread(() -> {
            synchronized (count) {
                for (char aChar : chars) {
                    System.out.print(aChar);
                    count.notify();
                    try {
                        count.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                count.notify();
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (count) {
                for (char aChar : chars1) {
                    System.out.print(aChar);
                    count.notify();
                    try {
                        count.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                count.notify();
            }
        }, "t2").start();
    }
}
