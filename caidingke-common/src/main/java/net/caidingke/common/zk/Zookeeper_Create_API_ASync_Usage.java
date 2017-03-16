package net.caidingke.common.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 创建节点，使用异步（async）接口
 *
 * @author bowen
 * @create 2017-03-02 15:47
 */

public class Zookeeper_Create_API_ASync_Usage implements Watcher {

    private static final String HOST = "localhost:2181,localhost:2182,localhost:2183";

    private static  final int TIME_OUT = 3000;

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        ZooKeeper zooKeeper = new ZooKeeper(HOST, TIME_OUT, new Zookeeper_Create_API_ASync_Usage());

        connectedSemaphore.await();

        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallback(), "I am context.");

        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallback(), "I am context.");
    }

    static class IStringCallback implements AsyncCallback.StringCallback {

        @Override
        public void processResult(int i, String s, Object o, String s1) {
            System.out.println("Create path result: " + i + "==" + s + "==" + o + "==" + s1);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
