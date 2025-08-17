package part3.client.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {

    InetSocketAddress discoverService(String serviceName);
}
