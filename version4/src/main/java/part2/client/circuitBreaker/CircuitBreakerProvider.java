package part2.client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

public class CircuitBreakerProvider {
    private static final Map<String,CircuitBreaker> CIRCUIT_BREAKER_MAP = new HashMap<>();

    public CircuitBreaker getCircuitBreaker(String interfaceName) {
        CircuitBreaker circuitBreaker;
        if(CIRCUIT_BREAKER_MAP.containsKey(interfaceName)){
            circuitBreaker=CIRCUIT_BREAKER_MAP.get(interfaceName);
        }else {
            System.out.println("serviceName="+interfaceName+"创建一个新的熔断器");
            circuitBreaker=new CircuitBreaker(1,0.5,10000);
            CIRCUIT_BREAKER_MAP.put(interfaceName,circuitBreaker);
        }
        return circuitBreaker;
    }
}
