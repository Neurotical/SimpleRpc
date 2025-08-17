package part3.common.message;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1069334427106347142L;

    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] parameterTypes;
}

