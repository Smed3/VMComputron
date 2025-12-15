package com.vmcomputron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProgramTextRequest(
        String filename,
        String code,
        boolean runAfterLoad
) {}
