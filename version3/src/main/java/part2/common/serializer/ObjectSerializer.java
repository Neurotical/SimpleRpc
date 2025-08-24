package part2.common.serializer;

import part2.common.enums.SerializerType;

import java.io.*;

/**
 * 基于java自带的序列化器
 */
public class ObjectSerializer implements Serializer {

    /**
     * 将数据写至字节输出流的缓存区，并读取对应字节数据
     *
     * @param obj
     * @return
     */
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();

            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();

            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public int getSerializeType() {
        return SerializerType.OBJECT_SERIALIZER.getValue();
    }
}
