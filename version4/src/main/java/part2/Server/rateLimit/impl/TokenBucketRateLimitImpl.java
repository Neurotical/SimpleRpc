package part2.Server.rateLimit.impl;

import part2.Server.rateLimit.RateLimit;

public class TokenBucketRateLimitImpl implements RateLimit {
    // 桶容量上限
    private final Integer CAPACITY;
    // 令牌生成速率
    private final Integer RATE;
    private Integer curCapacity;
    private Long lastTime;


    public TokenBucketRateLimitImpl(Integer capacity,Integer rate) {
        this.CAPACITY = capacity;
        this.RATE = rate;
        this.curCapacity = capacity;
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean getToken() {
        if (this.curCapacity > 0){
            this.curCapacity--;
            return true;
        }

        long curTime = System.currentTimeMillis();
        //判断是否有新生成的令牌
        if (curTime - lastTime > RATE) {
            if (curTime - lastTime >= RATE * 2){
                curCapacity = (int)Math.min(CAPACITY , (curTime - lastTime)/RATE - 1);
            }
            lastTime = curTime;
            return true;
        }

        return false;
    }
}
