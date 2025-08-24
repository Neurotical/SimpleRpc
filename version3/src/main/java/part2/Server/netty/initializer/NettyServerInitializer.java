package part2.Server.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import part2.Server.netty.handler.NettyServerHandler;
import part2.Server.provider.ServiceProvider;
import part2.common.nettyMsgChange.MyDecoder;
import part2.common.nettyMsgChange.MyEncoder;
import part2.common.serializer.JsonSerializer;

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
