package part2.common.enums;

public enum SerializerType {
    OBJECT_SERIALIZER(0),
    JSON_SERIALIZER(1);
    private final int value;

    SerializerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
