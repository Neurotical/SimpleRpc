package part2.client.proxy;

import part2.client.circuitBreaker.CircuitBreaker;
import part2.client.circuitBreaker.CircuitBreakerProvider;
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
    private CircuitBreakerProvider circuitBreakerProvider;

    public ClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public ClientProxy() throws InterruptedException {
        this.serviceCenter = new ZKServiceCenterImpl();
        this.rpcClient = new NettyClient(this.serviceCenter);
        this.circuitBreakerProvider=new CircuitBreakerProvider();
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

        CircuitBreaker circuitBreaker = circuitBreakerProvider.getCircuitBreaker(request.getInterfaceName());
        if (!circuitBreaker.allowRequest()){
            System.out.println("circuitBreaker not allow request");
            return null;
        }

        RpcResponse rpcResponse;
        //在重传白名单中时进行重传
        if (serviceCenter.checkRetry(request.getInterfaceName())){
            System.out.println(request.getInterfaceName() + " retry");
            rpcResponse = new GuavaRetry().sendRequestWithRetry(request, rpcClient);
        }
        else{
            rpcResponse = rpcClient.sendRequest(request);
        }

        //记录response的状态，上报给熔断器
        if (rpcResponse.getCode() ==200){
            circuitBreaker.recordSuccess();
        }
        if (rpcResponse.getCode()==500){
            circuitBreaker.recordFailure();
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
