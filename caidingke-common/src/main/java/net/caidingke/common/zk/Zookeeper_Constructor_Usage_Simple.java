package net.caidingke.common.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 创建一个最基本的zookeeper会话实例
 *
 * @author bowen
 * @create 2017-03-02 12:27
 */

public class Zookeeper_Constructor_Usage_Simple implements Watcher {

    private static final String HOST = "localhost:2181,localhost:2182,localhost:2183";

    private static  final int TIME_OUT = 3000;

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zookeeper = new ZooKeeper(HOST, TIME_OUT, new Zookeeper_Constructor_Usage_Simple());
        System.out.println(zookeeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Zookeeper session established.");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
