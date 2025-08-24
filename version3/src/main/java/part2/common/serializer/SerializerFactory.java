package part2.common.serializer;

import part2.common.enums.SerializerType;

public class SerializerFactory {
    public static Serializer getSerializer(int type) {
        if (type == SerializerType.OBJECT_SERIALIZER.getValue()) {
            return new ObjectSerializer();
        } else if (type == SerializerType.JSON_SERIALIZER.getValue()) {
            return new JsonSerializer();
        }
        return null;
    }
}
