package net.caidingke.zookeeper.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 一个典型时间戳生成的并发问题
 *
 * @author bowen
 * @create 2017-03-02 18:18
 */

public class Recipes_NoLock {

    public static void main(String[] args) {

        final CountDownLatch downLatch = new CountDownLatch(1);

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        downLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSSS");
                    String orderNo = sdf.format(new Date());
                    System.out.println("生成的订单号为：" + orderNo);
                }
            }).start();
        }
        downLatch.countDown();
    }
}
