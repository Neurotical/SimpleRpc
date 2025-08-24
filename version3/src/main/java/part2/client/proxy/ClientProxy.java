package part2.client.proxy;

import part2.client.retry.GuavaRetry;
import part2.client.rpcClient.RpcClient;
import part2.client.rpcClient.impl.NettyClient;
import part2.client.serviceCenter.ServiceCenter;
import part2.client.serviceCenter.impl.ZKServiceCenterImpl;
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
    private ServiceCenter serviceCenter;

    public ClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public ClientProxy() throws InterruptedException {
        this.serviceCenter = new ZKServiceCenterImpl();
        this.rpcClient = new NettyClient(this.serviceCenter);
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

        RpcResponse rpcResponse;
        //在重传白名单中时进行重传
        if (serviceCenter.checkRetry(request.getInterfaceName())){
            System.out.println(request.getInterfaceName() + " retry");
            rpcResponse = new GuavaRetry().sendRequestWithRetry(request, rpcClient);
        }
        else{
            rpcResponse = rpcClient.sendRequest(request);
        }

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
