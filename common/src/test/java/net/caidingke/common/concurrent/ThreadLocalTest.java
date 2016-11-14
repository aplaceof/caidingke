package net.caidingke.common.concurrent;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * thread local
 *
 * @author bowen
 * @create 2016-11-14 17:30
 */

public class ThreadLocalTest extends TestCase {

    @Test
    public void testThreadLocal() {
        ThreadLocalExample.MyRunnable sharedRunnableInstance = new ThreadLocalExample.MyRunnable();
        Thread thread1 = new Thread(sharedRunnableInstance);
        Thread thread2 = new Thread(sharedRunnableInstance);
        thread1.start();
        thread2.start();

        for (int i = 0; i < 5; i++) {
            new Thread(new ThreadLocalExample.MyThread(i)).start();
        }
    }
}
