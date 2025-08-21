package part2.client.serviceCenter.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import part2.client.serviceCenter.ServiceCenter;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenterImpl implements ServiceCenter {
    private static final String ROOT_PATH = "SimpleRpc";
    private final CuratorFramework client;

    public ZKServiceCenterImpl() {
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ROOT_PATH)
                .build();

        //注意要启动
        this.client.start();

        System.out.println("ZKServiceCenterImpl created");
    }

    @Override
    public InetSocketAddress discoverService(String serviceName) {
        try {
            List<String> strings = client.getChildren().forPath("/" + serviceName);
            return parseAddressString(strings.get(0));
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
