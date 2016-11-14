package net.caidingke.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * 请求锁,使用资源,释放锁
 *
 * @author bowen
 * @create 2016-09-09 11:39
 */

public class ExampleClientThatLocks {

    private final InterProcessMutex lock;

    private final FakeLimitedResource resource;

    private final String clientName;


    public ExampleClientThatLocks(InterProcessMutex lock, FakeLimitedResource resource, String clientName) {
        this.lock = lock;
        this.resource = resource;
        this.clientName = clientName;
    }

    public ExampleClientThatLocks(CuratorFramework client, String lockPath, FakeLimitedResource resource, String clientName) {
        this.resource = resource;
        this.clientName = clientName;
        lock = new InterProcessMutex(client, lockPath);
    }

    public void doWork(long time, TimeUnit unit) throws Exception {

        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }

        try {

            System.out.println(clientName+"has the lock");

            resource.use();//access resource exclusively 独家访问资源

        } finally {

            System.out.println(clientName + "releasing the lock");

            lock.release();//always release the lock in a finally block 最终释放锁
        }
    }
}
