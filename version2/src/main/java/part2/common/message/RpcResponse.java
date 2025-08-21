package part2.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import part2.common.enums.ExceptionCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -4347201425423408804L;

    private int code;
    private String message;
    //用于在json转为class时提供对象类型
    private Class<?> dataType;
    private Object data;

    public static RpcResponse success(Object data) {
        return RpcResponse.builder()
                .code(ExceptionCode.SUCCESS.getCode())
                .dataType(data.getClass())
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
