package part2.client.circuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 熔断器
 */
public class CircuitBreaker {

    private CircuitBreakerStateEnum state = CircuitBreakerStateEnum.CLOSED;
    private AtomicInteger failureCount = new AtomicInteger(0);
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicInteger totalCount = new AtomicInteger(0);
    //失败次数阈值
    private final int failureThreshold;
    //半开启-》关闭状态的成功次数比例
    private final double halfOpenSuccessRate;
    //恢复时间
    private final long retryPeriod;
    //上一次失败时间
    private long lastFailureTime = 0;

    public CircuitBreaker(int failureThreshold, double halfOpenSuccessRate, long retryPeriod) {
        this.failureThreshold = failureThreshold;
        this.halfOpenSuccessRate = halfOpenSuccessRate;
        this.retryPeriod = retryPeriod;
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        switch (state){
            case OPEN:{
                if (now - lastFailureTime > retryPeriod) {
                    state = CircuitBreakerStateEnum.HALF_OPEN;
                    resetCounts();
                    return true;
                }
                return true;
            }
            case HALF_OPEN:{
                totalCount.incrementAndGet();
                return true;
            }
            case CLOSED:
            default:
                return true;
        }
    }

    public synchronized void recordSuccess() {
        if (state == CircuitBreakerStateEnum.HALF_OPEN) {
            successCount.incrementAndGet();
            if (successCount.get() > halfOpenSuccessRate * totalCount.get()) {
                state = CircuitBreakerStateEnum.CLOSED;
                resetCounts();
            }
        }
        else{
            resetCounts();
        }
    }

    public synchronized void recordFailure() {
        failureCount.incrementAndGet();
        if (state == CircuitBreakerStateEnum.CLOSED
        || state == CircuitBreakerStateEnum.HALF_OPEN) {
            lastFailureTime = System.currentTimeMillis();
            if (failureCount.get() > failureThreshold) {
                state = CircuitBreakerStateEnum.OPEN;
            }
        }
    }

    private void resetCounts(){
        failureCount.set(0);
        successCount.set(0);
        totalCount.set(0);
    }
}
