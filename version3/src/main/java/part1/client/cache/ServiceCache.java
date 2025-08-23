package part1.client.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCache {
    private static final Map<String, List<String>> serviceCache = new HashMap<>();

    //添加服务
    public static void addService(String serviceName, List<String> address) {
        if (serviceCache.containsKey(serviceName)) {
            serviceCache.get(serviceName).addAll(address);
            System.out.println("Service " + serviceName + "address " + address + " added to the cache");
        } else {
            serviceCache.put(serviceName, address);
        }
    }

    //更新服务
    public static void changeService(String serviceName, List<String> toDel, List<String> toAdd) {
        if (serviceCache.containsKey(serviceName)) {
            List<String> existList = serviceCache.get(serviceName);
            existList.removeAll(toDel);
            existList.addAll(toAdd);
        } else {
            System.out.println("Error: Service " + serviceName + " not added to the cache");
        }
    }

    //删除服务指定地址
    public static void deleteService(String serviceName, List<String> toDel) {
        if (serviceCache.containsKey(serviceName)) {
            serviceCache.get(serviceName).removeAll(toDel);
        }
    }

    //获取服务
    public static List<String> getService(String serviceName) {
        return serviceCache.getOrDefault(serviceName, null);
    }
}
