package com.yllu.concurrency_Diagnostics_lab.aspect;

import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionCounter;
import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionTag;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidationMetricsAspect {

    private final MeterRegistry meterRegistry;

    @Around("@annotation(com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionCounter)")
    public Object measureValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ExceptionCounter annotation = method.getAnnotation(ExceptionCounter.class);
        String operationTag = annotation.tag();
        ExceptionTag[] exceptionTags = annotation.exceptions();

        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            for (ExceptionTag exceptionTag : exceptionTags) {
                if (exceptionTag.exception().isAssignableFrom(ex.getClass())) {
                    Counter.builder("validation.errors")
                            .tag(operationTag, exceptionTag.tag())
                            .description("Number of validation errors thrown")
                            .register(meterRegistry)
                            .increment();
                    break;
                }
            }
            throw ex; // always propagate
        }
    }
}
