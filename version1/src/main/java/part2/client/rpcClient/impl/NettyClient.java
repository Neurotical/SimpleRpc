package part2.client.rpcClient.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part2.client.netty.initializer.NettyClientInitializer;
import part2.client.rpcClient.RpcClient;
import part2.common.message.RpcRequest;
import part2.common.message.RpcResponse;

public class NettyClient implements RpcClient {
    private static final Bootstrap bootstrap;

    static {
        bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    private String host;
    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            ChannelFuture sync = bootstrap.connect(this.host, this.port).sync();
            //channel表示一个连接的单位，类似socket
            Channel channel = sync.channel();
            channel.writeAndFlush(request);
            channel.closeFuture().sync();

            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();

            return rpcResponse;
        } catch (InterruptedException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
        }
        return null;
    }
}
