// src/main/java/com/vmcomputron/model/RegisterUpdateRequest.java
package com.vmcomputron.model;

public record RegisterUpdateRequest(
        String register,
        Integer newValue
) {}