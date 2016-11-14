package net.caidingke.common.lock;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自旋锁 'CAS'
 *
 * @author bowen
 * @create 2016-11-14 11:33
 *
 *
 *
 * CAS算法研究
 * CAS相当于乐观锁，但是无需回滚。 这个算法之所以能够有较高的性能，取决于操作的两个特性。
 * CAS底层是atom operation..所以能够很快的执行完。如果操作时间长，那其他线程的CAS操作 就会失败的很快。
 * 除非是特别高并发的极端情况，CAS一般第一次就能够成功，所以虽然在代码上是 一个while循环，但是一般只是一个线性操作。
 * 但在高并发的情况下，可能会循环重试，使用LongAdder替代 AtomicLong是一个不错的选择。
 *
 *
 */

public class SpinLock {

    private int count;

    //自旋锁状态.
    private AtomicInteger owner = new AtomicInteger(0);

    /**
     * 利用 CAS 实现自旋锁.
     */
    public void lock() {
        while (!owner.compareAndSet(0, 1))
            ;
    }

    /**
     * 利用 compareAndSet 来尝试更新 state，1 表示锁定状态，0 表示空闲，每个线程都尝试去取得锁的所有权，
     * 如果失败，进入 while 忙等待。compare-and-set 都是利用 CPU 提供的原子指令(一般是 test_and_set)来实现。
     * 这个实现没有问题，但是当考虑到多核状态下的高速缓存的时候，如果对于锁的争用非常激烈，
     * 那么会有很多的线程在 while 上的 compareAndSet 原子操作上等待，保存 state 的缓存行就会在多个 CPU 之间『颠簸』，导致了不必要的总线争用。
     * 为了减少总线争用，我们可以改进下 lock:
     *
     * 通过增加一个内循环 while (state.get() == 1)，让没有获得锁的线程在 state 状态上等待，避免原子操作，可以减少总线争用。
     * 每个忙等待的 CPU 都将获得 state 状态的共享副本，以后的循环都将在共享副本上执行，不会再产生总线通信。
     * 当占有锁的线程释放锁（原子地设置 state 为 0）的时候，
     * 这些共享的副本会失效（write-invalidate 策略，比如常见的 MESI 协议）或者被更新到最新状态（write-update 策略），这时候才会产生总线通信。
     */
    private void optimizeLock() {
        while (!owner.compareAndSet(0, 1))
        /**
         * 对于未能取得锁所有权的线程，在内层循环上等待
         * 因为获取了 state 一份共享的高速缓存副本，
         * 不会再进一步产生总线通信量
         */
            while (owner.get() == 1)
                ;
    }

    public void unlock() {
        owner.set(0);
    }

    /**
     * 递增count 使用自旋锁保护count.
     */
    public void increase() {

        lock();
        count++;
        unlock();
    }

    /**
     * 测试，开启 50 个线程递增 count，每个线程循环 10 万次 计算耗时。
     *
     * @param warnUp
     * @throws Exception
     */
    public static void runTests(boolean warnUp) throws Exception {
        int threads = 50;
        final int n = 100000;
        final SpinLock sl = new SpinLock();
        Thread[] ts = new Thread[threads];
        long start = System.nanoTime();
        final CyclicBarrier barrier = new CyclicBarrier(threads + 1);
        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        barrier.await();
                        for (int j = 0; j < n; j++)
                            sl.increase();
                        barrier.await();
                    } catch (Exception e) {

                    }
                }
            });
            ts[i].start();
        }
        barrier.await();
        barrier.await();
        for (Thread t : ts) {
            t.join();
        }
        long duration = (System.nanoTime() - start) / 1000000;
        if (!warnUp)
            System.out.println("count= " + sl.count + ",cost:" + duration
                    + "ms");

    }

    public static void main(String[] args) throws Exception {
        // 测试，先 warm up
        // runTests(true);
        // 实际测试
        runTests(false);
    }
}