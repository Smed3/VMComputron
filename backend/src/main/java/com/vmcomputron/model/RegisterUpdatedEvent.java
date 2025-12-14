// src/main/java/com/vmcomputron/RegisterUpdatedEvent.java
package com.vmcomputron.model;

public record RegisterUpdatedEvent(String register, int newValue) { }