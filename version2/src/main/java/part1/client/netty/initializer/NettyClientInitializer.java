package part1.client.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import part1.client.netty.handler.NettyClientHandler;
import part1.common.enums.SerializerType;
import part1.common.nettyMsgChange.MyDecoder;
import part1.common.nettyMsgChange.MyEncoder;
import part1.common.serializer.JsonSerializer;
import part1.common.serializer.SerializerFactory;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}
