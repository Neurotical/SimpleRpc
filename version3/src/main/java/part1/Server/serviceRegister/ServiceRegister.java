package part1.Server.serviceRegister;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    void registerService(String serviceName, InetSocketAddress address);
}
