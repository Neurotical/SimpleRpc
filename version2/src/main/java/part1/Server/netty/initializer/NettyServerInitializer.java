package part1.Server.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import part1.Server.netty.handler.NettyServerHandler;
import part1.Server.provider.ServiceProvider;
import part1.common.enums.SerializerType;
import part1.common.nettyMsgChange.MyDecoder;
import part1.common.nettyMsgChange.MyEncoder;
import part1.common.serializer.JsonSerializer;
import part1.common.serializer.SerializerFactory;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new NettyServerHandler(serviceProvider));
    }
}
