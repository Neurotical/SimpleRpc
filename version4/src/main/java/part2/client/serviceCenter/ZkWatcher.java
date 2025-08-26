package part2.client.serviceCenter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import part2.client.cache.ServiceCache;

import java.util.Collections;

public class ZkWatcher {
    private final CuratorFramework client;

    public ZkWatcher(CuratorFramework client) {
        this.client = client;
    }

    public void watchToUpdate(String path) {
        CuratorCache cache = CuratorCache.build(client, "/" + path);
        cache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                // 第一个参数：事件类型（枚举）
                // 第二个参数：节点更新前的状态、数据
                // 第三个参数：节点更新后的状态、数据
                switch (type.name()) {
                    //创建节点
                    case "NODE_CREATED": {// 监听器第一次执行时节点存在也会触发次事件
                        String[] pathList = parseChildDataPath(childData1);
                        if (pathList.length <= 2) {
                            break;
                        }
                        ServiceCache.addService(pathList[1], Collections.singletonList(pathList[2]));
                        break;
                    }
                    //更新节点
                    case "NODE_UPDATED": {
                        String[] oldPathList = parseChildDataPath(childData);
                        String[] newPathList = parseChildDataPath(childData1);

                        ServiceCache.changeService(newPathList[1],
                                Collections.singletonList(oldPathList[2]),
                                Collections.singletonList(newPathList[2]));
                    }
                    //删除节点
                    case "NODE_DELETED": {
                        String[] pathList = parseChildDataPath(childData);
                        if (pathList.length <= 2) {
                            break;
                        }
                        ServiceCache.deleteService(pathList[1], Collections.singletonList(pathList[2]));
                    }
                    default: {
                        break;
                    }
                }
            }
        });
        cache.start();
    }

    public String[] parseChildDataPath(ChildData childData) {
        String path = childData.getPath();
        return path.split("/");
    }
}
