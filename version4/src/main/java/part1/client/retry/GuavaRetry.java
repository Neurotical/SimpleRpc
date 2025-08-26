package part1.client.retry;

import com.github.rholder.retry.*;
import part1.client.rpcClient.RpcClient;
import part1.common.enums.ExceptionCode;
import part1.common.message.RpcRequest;
import part1.common.message.RpcResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaRetry {
    public RpcResponse sendRequestWithRetry(RpcRequest rpcRequest, RpcClient client) {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfException()
                .retryIfResult(response -> response.getCode() != ExceptionCode.SUCCESS.getCode())
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("Retrying " + attempt.getAttemptNumber());
                    }
                })
                .build();
        try {
            return retryer.call(() -> client.sendRequest(rpcRequest));
        } catch (ExecutionException | RetryException e) {
            e.printStackTrace();
            return RpcResponse.fail(ExceptionCode.FAIL.getCode(), e.getMessage());
        }
    }
}
