package net.caidingke.common.concurrent;

/**
 * Created by bowen on 16/5/26.
 */
public class Sync {

    synchronized public void test() {

        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }

        try {
            Thread.sleep(5000);
            System.out.println("xx");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void test2() {

        synchronized (this) {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }

        try {
            Thread.sleep(50000);
            System.out.println("ff");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Sync sync = new Sync();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                sync.test();
            }
        },"test");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                sync.test2();
            }
        }, "test2");
        thread2.start();
        thread1.start();

    }
}
