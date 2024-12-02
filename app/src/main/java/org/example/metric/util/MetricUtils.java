package org.example.metric.util;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;

@UtilityClass
public class MetricUtils {

    public Object executeWithMetrics(
        Timer timer,
        ProceedingJoinPoint joinPoint,
        Counter successCounter,
        Counter failureCounter
    ) throws Throwable {
        return timer.recordCallable(() -> {
                try {
                    Object result = joinPoint.proceed();
                    successCounter.increment(); // 성공 시 카운터 증가
                    return result;
                } catch (Throwable e) {
                    failureCounter.increment(); // 실패 시 카운터 증가
                    try {
                        throw e;
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        );
    }
}
