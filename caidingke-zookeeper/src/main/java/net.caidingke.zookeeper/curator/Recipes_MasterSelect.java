package net.caidingke.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用curator实现分布式master选举
 *
 * @author bowen
 * @create 2017-03-02 18:07
 */

public class Recipes_MasterSelect {

    private static final String HOST = "localhost:2181,localhost:2182,localhost:2183";

    private static final int TIME_OUT = 3000;

    private static final String PATH = "/bowen/fan";

    private static final String MASTER_PATH = "/curator_recipes_master_path";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .connectString(HOST)
            .sessionTimeoutMs(TIME_OUT)
            .build();

    public static void main(String[] args) throws InterruptedException {

        client.start();
        LeaderSelector selector = new LeaderSelector(client, MASTER_PATH, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为Master角色");
                Thread.sleep(3000);
                System.out.println("完成Master操作，释放Master权利");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
