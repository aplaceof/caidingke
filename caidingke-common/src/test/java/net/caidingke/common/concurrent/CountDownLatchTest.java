package net.caidingke.common.concurrent;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author bowen
 * @create 2016-11-14 17:46
 */

public class CountDownLatchTest extends TestCase {

    @Test
    public void testCountDownLatchs() {
        CountDownLatch readyLatch = new CountDownLatch(5);
        CountDownLatch startLatch = new CountDownLatch(1);
        for (int i = 0; i < 5; i++) {

            new Thread(new CountDownLatchExample.Runner(String.valueOf(i+1),readyLatch,startLatch)).start();
        }
        try {
            readyLatch.await();
            System.out.println("等待所有人员准备完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始跑");
        startLatch.countDown();
       /* final CountDownLatch latch = new CountDownLatch(3);

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {

            executor.execute(new Thread() {
                public void run() {
                    try {
                        System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                        Thread.sleep(5000);
                        System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
                        latch.countDown();
                        System.out.println("当前计数器的值:"+latch.getCount());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
        }

        try {
            latch.await();
            System.out.println("等待3个子线程执行完毕..."+Thread.currentThread().getName());
            System.out.println("3个子线程已经执行完毕"+Thread.currentThread().getName());
            System.out.println("继续执行主线程"+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
    }
}
