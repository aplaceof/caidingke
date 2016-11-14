package net.caidingke.common.zk;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 分布式锁
 *
 * @author bowen
 * @create 2016-09-09 11:36
 */

public class FakeLimitedResource {


    private final static AtomicBoolean inUse = new AtomicBoolean(false);

    public void use() throws InterruptedException  {


        if (!inUse.compareAndSet(false, true)) {
            throw new IllegalStateException("Needs to be used by one client at a time");
        }
        try {
            Thread.sleep((long) (3 * Math.random()));
        } finally {
            inUse.set(false);
        }
    }

    public static void main(String[] args) {
        inUse.set(true);
        System.out.println(!inUse.compareAndSet(false,true));
    }

}
