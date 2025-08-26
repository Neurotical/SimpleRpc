package part2.client.serviceCenter.loadBalance;

import java.util.List;

public interface LoadBalance {

    String balance(List<String> addressList);
    void addNode(String node);
    void removeNode(String node);
}
