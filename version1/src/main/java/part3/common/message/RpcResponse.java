package part3.common.message;

import lombok.Builder;
import lombok.Data;
import part3.common.enums.ExceptionCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class RpcResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -4347201425423408804L;

    private int code;
    private String message;
    private Object data;

    public static RpcResponse success(Object data) {
        return RpcResponse.builder()
                .code(ExceptionCode.SUCCESS.getCode())
                .data(data)
                .build();
    }

    public static RpcResponse fail(int code, String message) {
        return RpcResponse.builder()
                .code(code)
                .data(message)
                .build();
    }
}
