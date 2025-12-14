package com.vmcomputron.model;

public record ProgramTextRequest(
        String filename,
        String code,
        boolean runAfterLoad,
        Integer inputInt
) {}
