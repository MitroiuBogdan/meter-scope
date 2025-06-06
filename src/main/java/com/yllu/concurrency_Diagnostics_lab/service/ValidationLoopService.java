package com.yllu.concurrency_Diagnostics_lab.service;


import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionCounter;
import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionMonitoringTags;
import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionTag;
import com.yllu.concurrency_Diagnostics_lab.exceptions.ValidationException;
import com.yllu.concurrency_Diagnostics_lab.rest.ValidationRequest;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class ValidationLoopService {
    @Autowired
    MeterRegistry meterRegistry;

    @ExceptionCounter(
            tag = "validate_looping",
            exceptions = {
                    @ExceptionTag(exception = ValidationException.class, tag = ExceptionMonitoringTags.API_VALIDATION_EXCEPTION),
                    @ExceptionTag(exception = RuntimeException.class, tag = ExceptionMonitoringTags.RUNTIME_EXCEPTION)
            }
    )
    public void runValidationLoop(ValidationRequest stats) {
        if (stats.count_errors() > 3) {
            throw new ValidationException("validation_except", "Message");
        } else {
            throw new RuntimeException("runtime-exception");
        }
    }


    public void testTimed(Integer delay) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Thread.sleep(delay);
            sample.stop(Timer.builder("process.time")
                    .description("Execution time for user processing")
                    .register(meterRegistry));
        } catch (Exception e) {
            System.out.println("Interruption");
        }
        System.out.println("Task was done");
    }
}
