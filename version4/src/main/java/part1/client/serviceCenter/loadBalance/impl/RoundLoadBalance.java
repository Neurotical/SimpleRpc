package part1.client.serviceCenter.loadBalance.impl;

import part1.client.serviceCenter.loadBalance.LoadBalance;

import java.util.List;

public class RoundLoadBalance implements LoadBalance {
    private static Integer chosen = -1;

    @Override
    public String balance(List<String> addressList) {
        chosen ++;
        return addressList.get(chosen % addressList.size());
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void removeNode(String node) {

    }
}
