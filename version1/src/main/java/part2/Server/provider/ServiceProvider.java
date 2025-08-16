package part2.Server.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地注册中心
 */
public class ServiceProvider {
    private final Map<String,Object> serviceMap;

    public ServiceProvider(Map<String,Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public ServiceProvider() {
        serviceMap = new HashMap<String,Object>();
    }

    //本地注册
    public void registerService(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> i : interfaces) {
            serviceMap.put(i.getName(), service);
        }
    }

    //获取服务实例
    public Object getService(String interfaceName) {
        return serviceMap.get(interfaceName);
    }
}
