package part2.Server.serviceRegister;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    void registerService(String serviceName, InetSocketAddress address, boolean retry);
}
