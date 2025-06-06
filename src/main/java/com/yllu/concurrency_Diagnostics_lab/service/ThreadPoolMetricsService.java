package com.yllu.concurrency_Diagnostics_lab.service;// src/main/java/com/example/demo/service/ThreadPoolMetricsService.java


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * Service class to demonstrate programmatic access to ThreadPoolTaskExecutor metrics
 * using Spring's MeterRegistry.
 */
@Service
public class ThreadPoolMetricsService {

    private final MeterRegistry meterRegistry;
    private final ThreadPoolTaskExecutor taskExecutor; // Inject the ThreadPoolTaskExecutor instance

    /**
     * Constructor for ThreadPoolMetricsService.
     * Injects MeterRegistry for metric collection and the custom taskExecutor.
     *
     * @param meterRegistry The Micrometer MeterRegistry bean.
     * @param taskExecutor The ThreadPoolTaskExecutor bean defined in AsyncConfig.
     */
    public ThreadPoolMetricsService(MeterRegistry meterRegistry,
                                    @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.meterRegistry = meterRegistry;
        this.taskExecutor = taskExecutor; // Store the injected executor instance
        // Register gauges for the executor's metrics
        registerExecutorMetrics();
    }

    /**
     * Registers custom gauges for the ThreadPoolTaskExecutor metrics.
     * While Spring Boot Actuator often auto-registers these, explicitly registering
     * them ensures they are available and allows for custom tagging if needed.
     * This also provides the correct instance for the method references.
     */
    private void registerExecutorMetrics() {
        // Register gauge for active threads
        meterRegistry.gauge("executor.active",
                io.micrometer.core.instrument.Tags.of("name", "taskExecutor"),
                taskExecutor, // Pass the actual instance here
                ThreadPoolTaskExecutor::getActiveCount);

        // Register gauge for queued tasks
        meterRegistry.gauge("executor.queued",
                io.micrometer.core.instrument.Tags.of("name", "taskExecutor"),
                taskExecutor, // Pass the actual instance here
                ThreadPoolTaskExecutor::getQueueSize);

        // Register gauge for current pool size
        meterRegistry.gauge("executor.pool.size",
                io.micrometer.core.instrument.Tags.of("name", "taskExecutor"),
                taskExecutor, // Pass the actual instance here
                ThreadPoolTaskExecutor::getPoolSize);

        // Note: "executor.completed" is typically a Counter and is automatically registered by Micrometer
        // when tasks are completed by a ThreadPoolTaskExecutor.
        // You can find it using meterRegistry.find() as shown in getThreadPoolMetrics().
    }


    /**
     * Retrieves and logs the current metrics of the ThreadPoolTaskExecutor.
     * This method demonstrates how to programmatically read the metric values.
     */
    public void logThreadPoolMetrics() {
        System.out.println("\n--- Current ThreadPoolTaskExecutor Metrics (taskExecutor) ---");

        // Retrieve active threads (gauge)
        // Micrometer automatically updates the gauge by calling getActiveCount() on 'taskExecutor'
        Double activeThreads = meterRegistry.find("executor.active")
                .tag("name", "taskExecutor")
                .gauge()
                .value();
        System.out.println("Active Threads: " + activeThreads);

        // Retrieve queued tasks (gauge)
        Double queuedTasks = meterRegistry.find("executor.queued")
                .tag("name", "taskExecutor")
                .gauge()
                .value();
        System.out.println("Queued Tasks: " + queuedTasks);

        // Retrieve current pool size (gauge)
        Double poolSize = meterRegistry.find("executor.pool.size")
                .tag("name", "taskExecutor")
                .gauge()
                .value();
        System.out.println("Pool Size: " + poolSize);

        // Retrieve completed tasks (counter)
        Counter completedTasksCounter = meterRegistry.find("executor.completed")
                .tag("name", "taskExecutor")
                .counter();
        if (completedTasksCounter != null) {
            System.out.println("Completed Tasks: " + completedTasksCounter.count());
        } else {
            System.out.println("Completed Tasks: Not yet available or 0");
        }

        System.out.println("--------------------------------------------------\n");
    }
    @Scheduled(fixedRate = 3000) // Execute every 3000 milliseconds (3 seconds)
    public void scheduledLogThreadPoolMetrics() {
        logThreadPoolMetrics();
    }
}
