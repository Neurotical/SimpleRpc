package part2.client.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {

    InetSocketAddress discoverService(String serviceName);
}
