package part1.client;

import part1.common.message.RpcRequest;
import part1.common.message.RpcResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * socket通信客户端
 */
public class IOClient {

    public static RpcResponse sendRequest(String host, int port, RpcRequest request) {
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
