package part2.Server;

import part2.Server.provider.ServiceProvider;
import part2.Server.server.RpcServer;
import part2.Server.server.impl.NettyRpcServer;
import part2.common.service.UserService;
import part2.common.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        ServiceProvider provider = new ServiceProvider("127.0.0.1", 9999);
        provider.registerService(userService);

        System.out.println("Server started");
        RpcServer server = new NettyRpcServer(provider);
        server.start(9999);
    }
}
