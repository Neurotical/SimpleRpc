package part3.Server.server.impl;

import lombok.AllArgsConstructor;
import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;
import part3.Server.server.thread.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRpcServer implements RpcServer {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        try{
            ServerSocket socket = new ServerSocket(port);
            while(true){
                Socket client = socket.accept();
                System.out.println("client connected");
                System.out.println("client address"+client.getInetAddress().getHostAddress());
                new Thread(new WorkThread(client,serviceProvider)).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {

    }
}
