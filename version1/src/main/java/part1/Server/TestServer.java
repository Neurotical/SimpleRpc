package part1.Server;

import part1.Server.provider.ServiceProvider;
import part1.Server.server.RpcServer;
import part1.Server.server.impl.SimpleRpcServer;
import part1.Server.server.impl.ThreadPoolRpcServer;
import part1.common.service.UserService;
import part1.common.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        ServiceProvider provider = new ServiceProvider();
        provider.registerService(userService);

        System.out.println("Server started");
//        SimpleRpcServer server = new SimpleRpcServer(provider);
        ThreadPoolRpcServer server = new ThreadPoolRpcServer(provider);
        server.start(9876);
    }
}
