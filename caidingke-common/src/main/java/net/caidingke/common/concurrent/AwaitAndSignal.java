package net.caidingke.common.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * condition  await signal
 *
 * @author bowen
 * @create 2017-03-06 10:52
 */

public class AwaitAndSignal implements Runnable {

    private String print;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public AwaitAndSignal(String print) {
        this.print = print;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            lock.lock();
            condition.signalAll();
            System.out.println(print);
            try {
                condition.await();
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {

        new Thread(new AwaitAndSignal("A")).start();
        new Thread(new AwaitAndSignal("B")).start();
    }
}
