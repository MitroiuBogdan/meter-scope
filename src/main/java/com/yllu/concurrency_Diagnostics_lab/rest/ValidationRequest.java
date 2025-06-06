package com.yllu.concurrency_Diagnostics_lab.rest;

public record ValidationRequest(
    Integer iterations,
    Integer delay,
    Integer count_errors
) {}
