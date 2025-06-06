
package com.yllu.concurrency_Diagnostics_lab.rest;

import com.yllu.concurrency_Diagnostics_lab.model.MyRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/v1/my-resource")
public class MyController {

    private final ThreadPoolTaskExecutor taskExecutor;

    public MyController(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
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
}
