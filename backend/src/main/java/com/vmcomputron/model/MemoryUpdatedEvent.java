// src/main/java/com/vmcomputron/event/MemoryUpdatedEvent.java
package com.vmcomputron.model;

public record MemoryUpdatedEvent(int address, int newValue) { }