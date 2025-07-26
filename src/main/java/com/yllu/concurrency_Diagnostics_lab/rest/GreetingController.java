package com.yllu.concurrency_Diagnostics_lab.rest;

import com.yllu.concurrency_Diagnostics_lab.model.GreetingResponse;
import com.yllu.concurrency_Diagnostics_lab.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class GreetingController {

    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greet")
    public CompletableFuture<GreetingResponse> greet() {
        String traceId = MDC.get("traceId");
        log.info("Received /greet request [traceId={}]", traceId);

        return greetingService.getGreeting()
                .thenApply(response -> {
                    log.info("Sending response for /greet [traceId={}]: {}", traceId, response);
                    return response;
                });
    }
}
