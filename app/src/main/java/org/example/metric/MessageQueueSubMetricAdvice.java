package org.example.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.metric.util.MetricUtils;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MessageQueueSubMetricAdvice {

    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, Counter> successCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> failureCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();

    public MessageQueueSubMetricAdvice(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("@annotation(org.example.metric.MessageQueueSubMonitored)")
    public Object monitorExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        MessageQueueSubMonitored annotation = method.getAnnotation(MessageQueueSubMonitored.class);
        String name = annotation.topic(); // 메서드별 식별자

        Counter successCounter = successCounters.computeIfAbsent(
            name, key -> meterRegistry.counter("sub.success." + name, "method", key)
        );

        Counter failureCounter = failureCounters.computeIfAbsent(
            name, key -> meterRegistry.counter("sub.failure." + name, "method", key)
        );

        Timer timer = timers.computeIfAbsent(
            name,
            key -> Timer.builder("sub.time." + name).tag("method", key).register(meterRegistry)
        );

        return MetricUtils.executeWithMetrics(timer, joinPoint, successCounter, failureCounter);
    }
}
