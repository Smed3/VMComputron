package com.vmcomputron.model;

public record MemoryUpdateRequest(int address, int newValue) { }