package part2.client.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import part2.client.netty.handler.NettyClientHandler;
import part2.common.nettyMsgChange.MyDecoder;
import part2.common.nettyMsgChange.MyEncoder;
import part2.common.serializer.JsonSerializer;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}
