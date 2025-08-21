package part2.client.rpcClient.impl;

import part2.client.rpcClient.RpcClient;
import part2.common.message.RpcRequest;
import part2.common.message.RpcResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * socket通信客户端
 */
public class SocketClient implements RpcClient {
    private String host;
    private int port;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(request);
            out.flush();
            RpcResponse response = (RpcResponse) in.readObject();
            socket.close();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
