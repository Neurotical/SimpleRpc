package part2.common.nettyMsgChange;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import part2.common.enums.MessageType;
import part2.common.serializer.Serializer;
import part2.common.serializer.SerializerFactory;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)  {
        try {
            short messageType = byteBuf.readShort();
            if (messageType != MessageType.RPC_REQUEST.getCode() &&
                    messageType != MessageType.RPC_RESPONSE.getCode()) {
                System.out.println("未支持的对象类型");
                return;
            }

            short serializerType = byteBuf.readShort();
            Serializer serializer = SerializerFactory.getSerializer(serializerType);
            if (serializer == null) {
                throw new RuntimeException("未支持的序列化器");
            }

            int dataLength = byteBuf.readInt();
            byte[] data = new byte[dataLength];
            byteBuf.readBytes(data);

            list.add(serializer.deserialize(data, messageType));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
