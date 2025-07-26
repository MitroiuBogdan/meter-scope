package com.yllu.concurrency_Diagnostics_lab.service;

import com.yllu.concurrency_Diagnostics_lab.model.GreetingResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class GreetingService {

    private final Executor mdcExecutor;

    public GreetingService(@Qualifier("mdcExecutor") Executor mdcExecutor) {
        this.mdcExecutor = mdcExecutor;
    }

    public CompletableFuture<GreetingResponse> getGreeting() {
        String traceId = MDC.get("traceId");
        log.info("Entering getGreeting() with traceId={}", traceId);

        return CompletableFuture.supplyAsync(() -> {
            log.info("Inside async thread with traceId={}", MDC.get("traceId"));
            GreetingResponse response = new GreetingResponse("Bună ziua din viitor!");
            log.info("Exiting getGreeting() with traceId={}", MDC.get("traceId"));
            return response;
        }, mdcExecutor);
    }
}
