package com.vmcomputron.model;

public record MemoryCellChangedEvent(
        String[][] part1,
        String[][] part2,
        String[][] part3,
        String[][] part4
) { }