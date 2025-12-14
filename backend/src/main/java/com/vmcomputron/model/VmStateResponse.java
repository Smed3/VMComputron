package com.vmcomputron.model;

public record VmStateResponse(
        boolean ok,
        boolean running,
        boolean needsInput,
        String error,

        int pc,
        int sp,
        int a,
        int x,
        float r,
        int rh,
        int rl,
        int mAtPc,

        ConsoleLine lastConsoleLine
) {}
