package part1.client.serviceCenter.loadBalance.impl;

import part1.client.serviceCenter.loadBalance.LoadBalance;
import part1.common.util.HashUtils;

import java.util.*;


public class ConsistencyHashLoadBalance implements LoadBalance {
    private static final int VIRTUAL_NODES = 5;

    private TreeMap<Integer,String> hashShards = new TreeMap<>();

    private List<String> realNodeList = new ArrayList<>();

    private void init(List<String> addresses) {
        for (String address : addresses) {
            realNodeList.add(address);
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String vrNodeList = address + "/" + i;
                hashShards.put(HashUtils.getHash(vrNodeList),vrNodeList);
            }
        }
    }

    private String getServerAddress(String node,List<String> addressList){
        init(addressList);
        int hash = HashUtils.getHash(node);
        Integer key = null;
        SortedMap<Integer, String> subMap = hashShards.tailMap(hash);
        if (subMap.isEmpty()) {
            key = hashShards.lastKey();
        } else {
            key = subMap.firstKey();
        }
        String virtualNode = hashShards.get(key);
        return virtualNode.substring(0, virtualNode.indexOf("/"));
    }

    @Override
    public String balance(List<String> addressList) {
        String node = UUID.randomUUID().toString();
        return getServerAddress(node, addressList);
    }

    @Override
    public void addNode(String node) {
        if (!realNodeList.contains(node)) {
            realNodeList.add(node);
            System.out.println("真实节点[" + node + "] 上线添加");
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = HashUtils.getHash(virtualNode);
                hashShards.put(hash, virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
            }
        }
    }

    @Override
    public void removeNode(String node) {
        if (realNodeList.contains(node)) {
            realNodeList.remove(node);
            System.out.println("真实节点[" + node + "] 下线移除");
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = HashUtils.getHash(virtualNode);
                hashShards.remove(hash);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被移除");
            }
        }
    }
}
