package com.vmcomputron.model;

import java.util.List;

public record ProgramParseResponse(
        boolean ok,
        String savedPath,
        List<String> errors,
        Object parsed
) {}
