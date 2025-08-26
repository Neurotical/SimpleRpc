package part1.Server.provider;

import part1.Server.rateLimit.provider.RateLimitProvider;
import part1.Server.serviceRegister.ServiceRegister;
import part1.Server.serviceRegister.impl.ZKServiceRegisterImpl;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地注册中心
 */
public class ServiceProvider {
    private String host;
    private int port;
    private final Map<String, Object> serviceMap;
    private ServiceRegister serviceRegister;
    private RateLimitProvider rateLimitProvider;

    public ServiceProvider(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public ServiceProvider(String host, int port) {
        this.host = host;
        this.port = port;
        this.serviceMap = new HashMap<String, Object>();
        this.serviceRegister = new ZKServiceRegisterImpl();
        this.rateLimitProvider = new RateLimitProvider();
    }

    //本地注册
    public void registerService(Object service,boolean retry) {
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> i : interfaces) {
            serviceMap.put(i.getName(), service);
            //在注册中心注册服务
            serviceRegister.registerService(i.getName(), new InetSocketAddress(host, port),retry);
        }
    }

    //获取服务实例
    public Object getService(String interfaceName) {
        return serviceMap.get(interfaceName);
    }

    public RateLimitProvider getRateLimitProvider() {
        return rateLimitProvider;
    }
}
