package part2.common.serializer;

/**
 * 序列化器
 */
public interface Serializer {
    //编码
    byte[] serialize(Object obj);

    //解码
    Object deserialize(byte[] bytes, int messageType);

    //获取使用的序列化器类型
    int getSerializeType();
}
