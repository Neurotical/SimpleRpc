package part2.common.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import part2.common.enums.SerializerType;
import part2.common.message.RpcRequest;
import part2.common.message.RpcResponse;

/**
 * json序列化器
 */
public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType) {
            case 0: {//此时对应RPC_REQUEST
                RpcRequest req = JSON.parseObject(bytes, RpcRequest.class);
                Object[] params = new Object[req.getParameters().length];
                for (int i = 0; i < req.getParameters().length; i++) {
                    Class<?> parameterType = req.getParameterTypes()[i];
                    //判断每个对象类型是否和paramsTypes中的一致
                    if (!parameterType.isAssignableFrom(req.getParameters()[i].getClass())) {
                        //如果不一致，就行进行类型转换
                        //进行转换的可能情况
                        //1.json自动将数字转换为int或long，而需要的可能为特点类型
                        //2.自定义对象需显式转换
                        params[i] = JSONObject.toJavaObject((JSONObject) req.getParameters()[i], parameterType);
                    } else {
                        params[i] = req.getParameters()[i];
                    }
                }
                req.setParameters(params);
                obj = req;
                break;
            }
            case 1: {
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                if (!response.getDataType().isAssignableFrom(response.getData().getClass())) {
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(), response.getDataType()));
                }
                obj = response;
                break;
            }
            default:
                throw new RuntimeException("json解码不支持的对象类型");
        }
        return obj;
    }

    @Override
    public int getSerializeType() {
        return SerializerType.JSON_SERIALIZER.getValue();
    }
}
