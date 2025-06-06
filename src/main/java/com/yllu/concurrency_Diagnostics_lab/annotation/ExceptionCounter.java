package com.yllu.concurrency_Diagnostics_lab.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionCounter {
    String tag(); // for timing
    ExceptionTag[] exceptions() default {};
}
