package com.sx;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者、消费者问题
 * @author suxin
 * @since 2022/03/04
 */
public class SxText01 {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition producercondition = lock.newCondition();
    private static Condition consumerCondition = lock.newCondition();
    private static volatile Integer count = 0;
    private static int maxNum = 100;

    public static void main(String[] args) {
        // 注意看两种写法的输出内容的不同

        //  这种写法，不能边生产边消费，一个人只要开始生产或开始消费，他就是把所有的馒头消费完或者生产满才会放手让别人进行操作
        conAndPro1();

        // 这种写法，可以边生产边消费，一个人只要生产或者消费了一次，就换人，让别人上
        // conAndPro2();

    }

    private static void conAndPro2() {
        SxText01 sxText01 = new SxText01();
        for (int i = 0; i < 100; i++) {
            Consumer2 consumer2 = sxText01.new Consumer2();
            Producer2 producer2 = sxText01.new Producer2();
            new Thread(consumer2).start();
            new Thread(producer2).start();
        }
    }

    private static void conAndPro1() {
        SxText01 sxText01 = new SxText01();
        for (int i = 0; i < 100; i++) {
            Consumer1 consumer1 = sxText01.new Consumer1();
            Producer1 producer1 = sxText01.new Producer1();
            new Thread(consumer1).start();
            new Thread(producer1).start();
        }
    }

    private class Consumer1 implements Runnable {
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (count <= 0) {
                        System.out.println("饿死了，没有馒头了！" + Thread.currentThread().getName());
                        // 所有的消费者都暂停了，不进行消费了，因为没有吃的东西啦
                        consumerCondition.await();
                    }
                    count--;
                    System.out.println("我吃了一个馒头，还有几个馒头？" + count + "|||" + Thread.currentThread().getName());
                    // 叫醒所有的生产者
                    producercondition.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private class Producer1 implements Runnable {
        public void run() {
             while (true) {
                lock.lock();
                try {
                    while (count >= maxNum) {
                        System.out.println("馒头都装满了，还要干啥，要卷死啊！"  + Thread.currentThread().getName());
                        // 暂停所有的生产者，因为框已经满了
                        producercondition.await();
                    }
                    count++;
                    System.out.println("我造了一个馒头，还有几个馒头？" + count + "|||" + Thread.currentThread().getName());
                    // 叫醒所有的消费者（他们可以准备抢锁，然后吃馒头了）
                    consumerCondition.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private class Consumer2 implements Runnable {
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (count <= 0) {
                        System.out.println("饿死了，没有馒头了！" + Thread.currentThread().getName());
                        consumerCondition.await();
                    }
                    count--;
                    System.out.println("我吃了一个馒头，还有几个馒头？" + count + "|||" + Thread.currentThread().getName());
                    // 叫醒所有的生产者的消费者，让他们进行抢锁（根据抢到锁的人的身份不同，看他是吃馒头还是生产馒头）
                    producercondition.signalAll();
                    consumerCondition.signalAll();
                    // 这里其实是释放自己手里的这把锁（所以说下个抢到锁的有可能是消费者，也可能是生产者）
                    consumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private class Producer2 implements Runnable {
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (count >= maxNum) {
                        System.out.println("馒头都装满了，还要干啥，要卷死啊！"  + Thread.currentThread().getName());
                        producercondition.await();
                    }
                    count++;
                    System.out.println("我造了一个馒头，还有几个馒头？" + count + "|||" + Thread.currentThread().getName());
                    consumerCondition.signalAll();
                    producercondition.signalAll();
                    producercondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
