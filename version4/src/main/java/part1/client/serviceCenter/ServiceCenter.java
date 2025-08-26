package part1.client.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {

    InetSocketAddress discoverService(String serviceName);

    boolean checkRetry(String serviceName);
}
