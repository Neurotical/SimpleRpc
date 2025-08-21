package part2.common.nettyMsgChange;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import part2.common.enums.MessageType;
import part2.common.message.RpcRequest;
import part2.common.message.RpcResponse;
import part2.common.serializer.Serializer;

@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //写入数据的类型
        int messageType = -1;
        if (o instanceof RpcRequest) {
            messageType = MessageType.RPC_REQUEST.getCode();
        } else if (o instanceof RpcResponse) {
            messageType = MessageType.RPC_RESPONSE.getCode();
        }
        byteBuf.writeShort(messageType);

        //写入使用的序列化器的类型
        byteBuf.writeShort(serializer.getSerializeType());

        byte[] data = serializer.serialize(o);

        //写入数据长度
        byteBuf.writeInt(data.length);
        //写入序列化后的数据
        byteBuf.writeBytes(data);
    }
}
