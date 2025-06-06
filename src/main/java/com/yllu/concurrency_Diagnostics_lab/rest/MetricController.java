
package com.yllu.concurrency_Diagnostics_lab.rest;

import com.yllu.concurrency_Diagnostics_lab.exceptions.ValidationException;
import com.yllu.concurrency_Diagnostics_lab.model.MyRecord;
import com.yllu.concurrency_Diagnostics_lab.service.ValidationLoopService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.runAsync;


@RestController
@RequestMapping("/api/v1/my-resource")
public class MetricController {

    private final ThreadPoolTaskExecutor taskExecutor;
    private final MeterRegistry meterRegistry;
    private final ValidationLoopService validationLoopService;

    public MetricController(ThreadPoolTaskExecutor taskExecutor, MeterRegistry meterRegistry, ValidationLoopService validationLoopService) {
        this.taskExecutor = taskExecutor;
        this.meterRegistry = meterRegistry;
        this.validationLoopService = validationLoopService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<MyRecord>> processMyRecord(@RequestBody MyRecord requestBody) {
        System.out.println("Received request (Async): ID = " + requestBody.id() + ", Message = " + requestBody.message());
        System.out.println("Request received on thread: " + Thread.currentThread().getName());


        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2); // Pause for 2 seconds to mimic a delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                System.err.println("Async processing interrupted: " + e.getMessage());

                return ResponseEntity.internalServerError().build();
            }

            MyRecord responseRecord = new MyRecord(requestBody.id(), "Processed Async: " + requestBody.message());
            System.out.println("Processing complete on thread: " + Thread.currentThread().getName());


            return ResponseEntity.ok(responseRecord);
        }, taskExecutor);
    }

    @PostMapping("/counter")
    public CompletableFuture<ResponseEntity<ValidationRequest>> testCounter(@RequestBody ValidationRequest validationRequest) {
        return CompletableFuture.supplyAsync(() -> {
            runAsync(() -> {

                try {
                    validationLoopService.runValidationLoop(validationRequest);
                } catch (Exception e) {
                    System.out.println("Catch in the controller");

                }
            });
            return ResponseEntity.ok(validationRequest);
        });
    }
}
