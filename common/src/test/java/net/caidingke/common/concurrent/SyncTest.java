package net.caidingke.common.concurrent;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * sync
 *
 * @author bowen
 * @create 2016-11-14 17:34
 */

public class SyncTest extends TestCase {

    @Test
    public void testSync() {

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
        thread1.start();
        thread2.start();
    }
}
