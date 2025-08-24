package part2.client.serviceCenter.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import part2.client.cache.ServiceCache;
import part2.client.serviceCenter.ServiceCenter;
import part2.client.serviceCenter.ZkWatcher;
import part2.client.serviceCenter.loadBalance.impl.ConsistencyHashLoadBalance;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenterImpl implements ServiceCenter {
    private static final String ROOT_PATH = "SimpleRpc";
    private final CuratorFramework client;
    private ZkWatcher zkWatcher;

    public ZKServiceCenterImpl() throws InterruptedException {
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ROOT_PATH)
                .build();

        //注意要启动
        this.client.start();

        //启动监听器
        zkWatcher = new ZkWatcher(client);
        zkWatcher.watchToUpdate(ROOT_PATH);

        System.out.println("ZKServiceCenterImpl created");
    }

    @Override
    public InetSocketAddress discoverService(String serviceName) {
        try {
            List<String> cacheAddressList = ServiceCache.getService(serviceName);
            if (cacheAddressList == null || cacheAddressList.isEmpty()) {
                System.out.println("query directly from zookeeper:" + serviceName);
                cacheAddressList = client.getChildren().forPath("/" + serviceName);
                ServiceCache.addService(serviceName,cacheAddressList);
            }
            // 负载均衡得到地址
            String address = new ConsistencyHashLoadBalance().balance(cacheAddressList);
            return parseAddressString(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private InetSocketAddress parseAddressString(String address) {
        String[] split = address.split(":");
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }
}
