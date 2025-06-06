package com.yllu.concurrency_Diagnostics_lab.annotation;

import java.lang.annotation.*;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionTag {
    Class<? extends Throwable> exception();
    String tag();
}
