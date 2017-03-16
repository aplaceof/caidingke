package net.caidingke.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 使用fluent风格API接口创建一个zookeeper客户端
 *
 * @author bowen
 * @create 2017-03-02 17:08
 */

public class Create_Session_Sample_Fluent {

    private static final String HOST = "localhost:2181,localhost:2182,localhost:2183";

    private static final int TIME_OUT = 3000;

    private static final String PATH = "/bowen/fan";

    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(HOST)
                .sessionTimeoutMs(TIME_OUT)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        //create node
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(PATH, "init".getBytes());
        //Thread.sleep(Integer.MAX_VALUE);

        //delete node
        delete(client, PATH, true);

        client.setData().withVersion(2).forPath(PATH);
    }

    private static void delete(CuratorFramework client, String path, boolean ifNeeded) throws Exception {

        if (ifNeeded) {
            client.delete()
                    .deletingChildrenIfNeeded()
                    .forPath(path);
        } else {

            client.delete()
                    .forPath(path);

        }
    }

}
