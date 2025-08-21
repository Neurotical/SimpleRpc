package part2.common.enums;

public enum MessageType {
    RPC_REQUEST(0),
    RPC_RESPONSE(1),
    ;

    private final int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
