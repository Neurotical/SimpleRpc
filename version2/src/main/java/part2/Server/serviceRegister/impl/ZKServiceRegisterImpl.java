package part2.Server.serviceRegister.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import part2.Server.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;

/**
 * zookeeper注册服务实现
 */
public class ZKServiceRegisterImpl implements ServiceRegister {

    //zookeeper根路径节点
    private static final String ROOT_PATH = "SimpleRpc";
    // curator 提供的zookeeper客户端
    private final CuratorFramework client;

    public ZKServiceRegisterImpl() {
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))  //重传策略
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        System.out.println("zookeeper client started");
    }

    @Override
    public void registerService(String serviceName, InetSocketAddress address) {
        try {
            // serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath("/" + serviceName);
            }

            String addressString = "/" + serviceName + "/" + this.getIPAddress(address);
            // 临时节点，服务器下线就删除节点
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(addressString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private String getIPAddress(InetSocketAddress address) {
        return address.getHostName() + ":" + address.getPort();
    }

}
