package part1.Server.server.thread;

import lombok.AllArgsConstructor;
import part1.Server.provider.ServiceProvider;
import part1.common.enums.ExceptionCode;
import part1.common.message.RpcRequest;
import part1.common.message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable {
    private Socket socket;
    private ServiceProvider provider;

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            RpcRequest request = (RpcRequest) ois.readObject();
            RpcResponse response = this.getResponse(request);
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private RpcResponse getResponse(RpcRequest request) {
        try {
            Object service = provider.getService(request.getInterfaceName());
            Method method = service.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
            Object invoke = method.invoke(service, request.getParameters());

            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("invoke method error: " + e.getMessage());
            return RpcResponse.fail(ExceptionCode.FAIL.getCode(), e.getMessage());
        }
    }
}
