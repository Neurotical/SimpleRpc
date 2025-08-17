package part3.client.proxy;

import part3.client.rpcClient.RpcClient;
import part3.client.rpcClient.impl.NettyClient;
import part3.client.rpcClient.impl.SocketClient;
import part3.common.message.RpcRequest;
import part3.common.message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理
 */
public class ClientProxy implements InvocationHandler {
    private RpcClient rpcClient;

    public ClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public ClientProxy(String host, int port, int chosen) {
        switch (chosen) {
            case 0:
                this.rpcClient = new NettyClient(host, port);
                break;
            case 1:
                this.rpcClient = new SocketClient(host, port);
                break;
            default:
                break;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .parameterTypes(method.getParameterTypes())
                .build();

        System.out.println("request: " + request);

        RpcResponse rpcResponse = rpcClient.sendRequest(request);

        System.out.println("rpcResponse: " + rpcResponse);

        if (rpcResponse == null || rpcResponse.getData() == null) {
            return null;
        }

        return rpcResponse.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}
