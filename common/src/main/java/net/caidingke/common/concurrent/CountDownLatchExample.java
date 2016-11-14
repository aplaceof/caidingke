package net.caidingke.common.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by bowen on 16/3/28.
 */
public class CountDownLatchExample {
    private static Random RANDOM = new Random();

    static class Runner implements Runnable{

        private String name;

        private  CountDownLatch readyLatch ;

        private  CountDownLatch startLatch ;

        public Runner(String name, CountDownLatch readyLatch, CountDownLatch startLatch) {

            this.name = name;
            this.readyLatch = readyLatch;
            this.startLatch = startLatch;
        }
        @Override
        public void run() {
            int time = RANDOM.nextInt(1000);
            System.out.println(name +"需要"+time+"准备时间");
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("准备完毕");
            readyLatch.countDown();
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("开始");
        }
    }
}
