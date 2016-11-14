package net.caidingke.common.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by bowen on 16/3/28.
 */
public class CyclicBarrierExample {

    //等待线程都执行完毕,再执行
    public static void cyclicBarrier() {
        int j = 4;
        CyclicBarrier barrier  = new CyclicBarrier(j);
        for(int i = 0; i < j; i++)
            new Writer(barrier).start();
    }
    //等待线程都执行完毕,挑选一个线程去执行 new Runnable(){}
    public static void cyclicBarrierRunnable() {
        int j = 4;
        CyclicBarrier barrier  = new CyclicBarrier(j,new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程"+Thread.currentThread().getName());
            }
        });

        for(int i = 0; i < j; i++)
            new Writer(barrier).start();
    }
    //达到指定时间线程还没有执行完毕,抛出异常,并继续执行.
    public static void cyclicBarrierTime() {
        int j = 4;
        CyclicBarrier barrier  = new CyclicBarrier(j);

        for(int i = 0; i < j; i++) {
            if(i< j -1)
                new WriterTime(barrier).start();
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new WriterTime(barrier).start();
            }
        }
    }
    //CyclicBarrier重用实例
    public static void cyclicBarrierReusing() {
        int j = 4;
        CyclicBarrier barrier  = new CyclicBarrier(j);

        for(int i = 0; i < j; i++) {
            new Writer(barrier).start();
        }

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("CyclicBarrier重用实例");

        for(int i = 0; i< j; i++) {
            new Writer(barrier).start();
        }
    }

    static class WriterTime extends Thread{
        private CyclicBarrier cyclicBarrier;
        public WriterTime(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                //让线程睡眠,模仿写入操作
                Thread.sleep(5000);
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                try {
                    cyclicBarrier.await(2000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {

                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"所有线程写入完毕，继续处理其他任务...");
        }
    }

    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务..."+Thread.currentThread().getName());
        }

    }
}
