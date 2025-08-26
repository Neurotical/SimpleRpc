package part1.Server.rateLimit.provider;

import part1.Server.rateLimit.RateLimit;
import part1.Server.rateLimit.impl.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * 每个接口都对应一个限流器
 */
public class RateLimitProvider {
    private static final Map<String, RateLimit> rateLimitMap = new HashMap<String, RateLimit>();

    private final Object lock = new Object();

    /**
     * 获取接口对应的限流器
     * @param interfaceName
     * @return
     */
    public RateLimit getRateLimit(String interfaceName) {
        if (rateLimitMap.containsKey(interfaceName)) {
            return rateLimitMap.get(interfaceName);
        }

        synchronized (lock) {
            if (rateLimitMap.containsKey(interfaceName)) {
                return rateLimitMap.get(interfaceName);
            }
            RateLimit limit = new TokenBucketRateLimitImpl(100,10);
            rateLimitMap.put(interfaceName, limit);
            return limit;
        }
    }
}
