package part2.client.proxy;

import part2.client.rpcClient.RpcClient;
import part2.client.rpcClient.impl.NettyClient;
import part2.common.message.RpcRequest;
import part2.common.message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理
 */
public class ClientProxy implements InvocationHandler {
    private final RpcClient rpcClient;

    public ClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public ClientProxy() {
        this.rpcClient = new NettyClient();
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
