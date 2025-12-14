// src/main/java/com/vmcomputron/model/RegisterUpdateRequest.java
package com.vmcomputron.model;

public record RegisterChangedEvent(
        String register,
        Integer newValue
) {}