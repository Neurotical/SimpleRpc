package part3.Server.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import part3.Server.netty.initializer.NettyServerInitializer;
import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;

public class NettyRpcServer implements RpcServer {
    private ServiceProvider provider;

    public NettyRpcServer(ServiceProvider provider) {
        this.provider = provider;
    }

    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("Server started on port " + port);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(provider));
            //同步堵塞
            ChannelFuture sync = bootstrap.bind(port).sync();

            sync.channel().closeFuture().sync();
            System.out.println("Server end on port " + port);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
