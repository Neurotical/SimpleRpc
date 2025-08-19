package part1.Server.server.impl;

import part1.Server.provider.ServiceProvider;
import part1.Server.server.RpcServer;
import part1.Server.server.thread.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRpcServer implements RpcServer {
    private final ServiceProvider provider;

    private final ThreadPoolExecutor executor;

    public ThreadPoolRpcServer(ServiceProvider provider) {
        this.provider = provider;
        executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                100,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public ThreadPoolRpcServer(ServiceProvider provider, ThreadPoolExecutor executor) {
        this.provider = provider;
        this.executor = executor;
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket socket = new ServerSocket(port);

            while (true) {
                Socket accept = socket.accept();
                executor.execute(new Thread(new WorkThread(accept, provider)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {

    }
}
