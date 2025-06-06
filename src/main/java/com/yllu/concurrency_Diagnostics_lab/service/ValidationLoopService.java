package com.yllu.concurrency_Diagnostics_lab.service;


import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionCounter;
import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionMonitoringTags;
import com.yllu.concurrency_Diagnostics_lab.annotation.ExceptionTag;
import com.yllu.concurrency_Diagnostics_lab.exceptions.ValidationException;
import com.yllu.concurrency_Diagnostics_lab.rest.ValidationRequest;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class ValidationLoopService {

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
}
