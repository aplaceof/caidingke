package net.caidingke.common.concurrent;

/**
 * wait notify notifyAll 的使用
 *
 * @author bowen
 * @create 2017-03-06 10:20
 */

public class WaitAndNotify implements Runnable{
    private String lock;
    private String print;

    public WaitAndNotify(String lock, String print) {
        this.print = print;
        this.lock = lock;
    }

    @Override
    public void run() {

        synchronized (lock) {
            for (int i = 0; i < 10; i++) {

                lock.notifyAll();
                System.out.println(print);
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String lock = "3";
        new Thread(new WaitAndNotify(lock, "a")).start();
        Thread.sleep(1000);
        new Thread(new WaitAndNotify(lock, "b")).start();
    }
}
