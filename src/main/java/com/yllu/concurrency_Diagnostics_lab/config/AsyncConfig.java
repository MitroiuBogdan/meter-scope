
package com.yllu.concurrency_Diagnostics_lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class for setting up an asynchronous task executor.
 *
 * @EnableAsync enables Spring's asynchronous method execution capability.
 * When a method is annotated with @Async, Spring will execute it in a separate thread,
 * typically managed by the Executor defined here.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Defines a custom ThreadPoolTaskExecutor bean.
     * This executor will be used by default for @Async methods and can be explicitly
     * used with CompletableFuture.supplyAsync or CompletableFuture.runAsync.
     *
     * @return The configured ThreadPoolTaskExecutor.
     */
    @Bean(name = "taskExecutor") // Give it a name for explicit referencing
    public ThreadPoolTaskExecutor taskExecutor() { // Changed return type to ThreadPoolTaskExecutor
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Set the core number of threads. These threads are always kept alive.
        executor.setCorePoolSize(5);

        // Set the maximum number of threads allowed in the pool.
        executor.setMaxPoolSize(10);

        // Set the capacity of the queue for holding tasks before they are executed.
        // If the core pool is full, tasks are added to this queue.
        // If the queue is full, new threads up to maxPoolSize are created.
        executor.setQueueCapacity(25);

        // Set the prefix for thread names created by this executor.
        // Useful for debugging and monitoring thread activity.
        executor.setThreadNamePrefix("MyAsync-");

        // Initialize the executor. This must be called after setting properties.
        executor.initialize();

        System.out.println("ThreadPoolTaskExecutor 'taskExecutor' initialized with:" +
                           "\n  Core Pool Size: " + executor.getCorePoolSize() +
                           "\n  Max Pool Size: " + executor.getMaxPoolSize() +
                           "\n  Queue Capacity: " + executor.getQueueCapacity() +
                           "\n  Thread Name Prefix: " + executor.getThreadNamePrefix());

        return executor;
    }
}
