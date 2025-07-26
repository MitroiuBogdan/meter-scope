package com.yllu.concurrency_Diagnostics_lab.service;

import org.slf4j.MDC;

import java.util.Map;
import java.util.function.Supplier;

public class MdcAware {

    public static Runnable wrap(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static <T, R> java.util.function.Function<T, R> wrap(java.util.function.Function<T, R> function) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return (T input) -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                return function.apply(input);
            } finally {
                MDC.clear();
            }
        };
    }


    public static <T> Supplier<T> wrap(Supplier<T> supplier) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                return supplier.get();
            } finally {
                MDC.clear();
            }
        };
    }
}
