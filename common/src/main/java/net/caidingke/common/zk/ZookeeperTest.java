package net.caidingke.common.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by bowen on 16/3/25.
 */
public class ZookeeperTest {

    private static final String HOST = "localhost:2181,localhost:2182,localhost:2183";

    private static  final int TIME_OUT = 3000;

    private String groupNode = "locks";

    private String subNode = "sub";

    private ZooKeeper zk;

    // 当前client创建的子节点
    private String thisPath;

    // 当前client等待的子节点
    private String waitPath;

    private CountDownLatch latch = new CountDownLatch(1);

    public void connectZookeeper() throws Exception{

        zk = new ZooKeeper(HOST, TIME_OUT, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                try {
                    // 连接建立时, 打开latch, 唤醒wait在该latch上的线程
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                        latch.countDown();
                    }
                    //发生了waitPath的删除事件.
                    if (watchedEvent.getType() == Event.EventType.NodeDeleted && watchedEvent.getPath().equals(waitPath)) {
                        doSomething();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //等待链接
        latch.await();

        //创建子节点
        thisPath = zk.create("/" + groupNode + "/" + subNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        //让wait睡一会,让结果更加清晰
        Thread.sleep(10);

        // 注意, 没有必要监听"/locks"的子节点的变化情况
        List<String> childrenNodes = zk.getChildren("/" + groupNode, false);

        // 列表中只有一个子节点, 那肯定就是thisPath, 说明client获得锁
        if (childrenNodes.size() == 1) {
            doSomething();
        }else {

            String thisNode = thisPath.substring(("/" + groupNode + "/").length());

            //排序
            Collections.sort(childrenNodes);

            int index = childrenNodes.indexOf(thisNode);

            if (index == 1) {

                //never happened

            } else if (index == 0) {

                //index == 0 .说明thisNode在列表中最小,当前client获得锁
                doSomething();
            } else {

                // 获得排名比thisPath前1位的节点
                this.waitPath = "/" + groupNode + "/" + childrenNodes.get(index - 1);

                // 在waitPath上注册监听器, 当waitPath被删除时, zookeeper会回调监听器的process方法
                zk.getData(waitPath, true, new Stat());
            }
        }
    }

    /**
     * 共享资源的访问逻辑写在这个方法中
     */
    private void doSomething() throws Exception {
        try {
            System.out.println("gain lock: " + thisPath);
            Thread.sleep(2000);
            // do something
        } finally {
            System.out.println("finished: " + thisPath);
            // 将thisPath删除, 监听thisPath的client将获得通知
            // 相当于释放锁
            zk.delete(this.thisPath, -1);
        }
    }

    public static void main(String[] args) throws Exception{


//        for (int i = 0; i < 10; i++) {
//            new Thread() {
//                public void run() {
//                    try {
//                        ZookeeperTest dl = new ZookeeperTest();
//                        dl.connectZookeeper();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        }
//
//        Thread.sleep(Long.MAX_VALUE);
        zoo();


    }

    public static void zk() throws Exception {

        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper(HOST, TIME_OUT, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("EVENT:" + event.getType());
            }
        });

        // 查看根节点
        System.out.println("ls / => " + zk.getChildren("/", true));

        // 创建一个目录节点
        if (zk.exists("/node", true) == null) {
            zk.create("/node", "conan".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node conan");
            // 查看/node节点数据
            System.out.println("get /node => " + new String(zk.getData("/node", false, null)));
            // 查看根节点
            System.out.println("ls / => " + zk.getChildren("/", true));
        }

        // 创建一个子目录节点
        if (zk.exists("/node/sub1", true) == null) {
            zk.create("/node/sub1", "sub1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node/sub1 sub1");
            // 查看node节点
            System.out.println("ls /node => " + zk.getChildren("/node", true));
        }

        // 修改节点数据
        if (zk.exists("/node", true) != null) {
            zk.setData("/node", "changed".getBytes(), -1);
            // 查看/node节点数据
            System.out.println("get /node => " + new String(zk.getData("/node", false, null)));
        }

        // 删除节点
//        if (zk.exists("/node/sub1", true) != null) {
//            zk.delete("/node/sub1", -1);
//            zk.delete("/node", -1);
//            // 查看根节点
//            System.out.println("ls / => " + zk.getChildren("/", true));
//        }

        // 关闭连接
        zk.close();
    }

    public static void zoo() throws Exception {

                zk();

        ZooKeeper zookeeper = new ZooKeeper(HOST, TIME_OUT, null);

        System.out.println("=========创建节点===========");
        if(zookeeper.exists("/test", false) == null)
        {
            zookeeper.create("/test", "znode1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        System.out.println("=============查看节点是否安装成功===============");
        System.out.println(new String(zookeeper.getData("/test", false, null)));

        System.out.println("=========修改节点的数据==========");
        String data = "zNode2";
        zookeeper.setData("/test", data.getBytes(), -1);

        System.out.println("========查看修改的节点是否成功=========");
        System.out.println(new String(zookeeper.getData("/test", false, null)));
        System.out.println(zookeeper.getChildren("/test", false));

        System.out.println("=======删除节点==========");
        zookeeper.delete("/test", -1);

        System.out.println("==========查看节点是否被删除============");
        System.out.println("节点状态：" + zookeeper.exists("/test", false));
        System.out.println(zookeeper.exists("/elasticjob-example",false));
        System.out.println(zookeeper.exists("/elastic-job-example",false));

//        zookeeper.delete("/elastic-job-example", -1);
//        zookeeper.delete("/elasticjob-example", -1);


        zookeeper.close();
    }
}
