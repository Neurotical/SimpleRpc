package part1.client.rpcClient.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part1.client.netty.initializer.NettyClientInitializer;
import part1.client.rpcClient.RpcClient;
import part1.client.serviceCenter.ServiceCenter;
import part1.client.serviceCenter.impl.ZKServiceCenterImpl;
import part1.common.message.RpcRequest;
import part1.common.message.RpcResponse;

import java.net.InetSocketAddress;

public class NettyClient implements RpcClient {
    private static final Bootstrap bootstrap;

    static {
        bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    private final ServiceCenter serviceCenter;

    public NettyClient() {
        this.serviceCenter = new ZKServiceCenterImpl();
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        //发现服务,获取服务ip+port
        InetSocketAddress inetSocketAddress = this.serviceCenter.discoverService(request.getInterfaceName());
        if (inetSocketAddress == null) {
            System.out.println("服务未找到:" + request.getInterfaceName());
            return null;
        }
        String host = inetSocketAddress.getHostString();
        int port = inetSocketAddress.getPort();

        try {
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            System.out.println("connected to " + host + ":" + port);
            //channel表示一个连接的单位，类似socket
            Channel channel = sync.channel();
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            System.out.println("send request finished");

            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();

            return rpcResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
