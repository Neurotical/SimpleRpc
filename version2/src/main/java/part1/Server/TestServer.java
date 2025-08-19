package part1.Server;

import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;
import part3.Server.server.impl.NettyRpcServer;
import part3.common.service.UserService;
import part3.common.service.impl.UserServiceImpl;

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
