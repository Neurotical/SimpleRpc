package part1.client.proxy;

import lombok.AllArgsConstructor;
import part1.client.IOClient;
import part1.common.message.RpcRequest;
import part1.common.message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    private final String host;
    private final int port;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .parameterTypes(method.getParameterTypes())
                .build();

        RpcResponse rpcResponse = IOClient.sendRequest(host, port, request);

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
