package net.caidingke.common.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author bowen
 * @create 2017-03-02 15:23
 */

public class Zookeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {

    private static final String HOST = "localhost:2181,localhost:2182,localhost:2183";

    private static  final int TIME_OUT = 3000;

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {

        ZooKeeper zooKeeper = new ZooKeeper(HOST, TIME_OUT, new Zookeeper_Constructor_Usage_With_SID_PASSWD());

        connectedSemaphore.await();

        long sessionId = zooKeeper.getSessionId();

        byte[] sessionPassWd = zooKeeper.getSessionPasswd();

        //Use illegal sessionId and sessionPassWd
        zooKeeper = new ZooKeeper(HOST, TIME_OUT, new Zookeeper_Constructor_Usage_With_SID_PASSWD(),
                1L, "test".getBytes());

        //Use correct sessionId and sessionPassWd

        zooKeeper = new ZooKeeper(HOST, TIME_OUT, new Zookeeper_Constructor_Usage_With_SID_PASSWD(),
                sessionId, sessionPassWd);
        Thread.sleep(Integer.MAX_VALUE);

    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event: " + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
