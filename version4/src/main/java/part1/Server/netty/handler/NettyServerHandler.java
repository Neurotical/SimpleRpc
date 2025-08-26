package part1.Server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import part1.Server.provider.ServiceProvider;
import part1.common.enums.ExceptionCode;
import part1.common.message.RpcRequest;
import part1.common.message.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private ServiceProvider provider;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        System.out.println("Received RPC request: " + rpcRequest);
        RpcResponse rpcResponse = this.getResponse(rpcRequest);

        channelHandlerContext.writeAndFlush(rpcResponse);
        channelHandlerContext.close();
    }

    private RpcResponse getResponse(RpcRequest rpcRequest) {
        String interfaceName = rpcRequest.getInterfaceName();
        if(!provider.getRateLimitProvider().getRateLimit(interfaceName).getToken()){
            System.out.println("服务限流");
            return RpcResponse.fail(ExceptionCode.FAIL.getCode(), "服务限流!");
        }

        try {
            Object service = provider.getService(rpcRequest.getInterfaceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object invoke = method.invoke(service, rpcRequest.getParameters());
            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return RpcResponse.fail(ExceptionCode.FAIL.getCode(), e.getMessage());
        }
    }
}
