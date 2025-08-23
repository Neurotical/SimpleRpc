package part1.client.serviceCenter.loadBalance.impl;

import part1.client.serviceCenter.loadBalance.LoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    private static final Random random = new Random();

    @Override
    public String balance(List<String> addressList) {
        return addressList.get(random.nextInt(addressList.size()));
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void removeNode(String node) {

    }
}
