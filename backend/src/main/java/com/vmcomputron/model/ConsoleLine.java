package com.vmcomputron.model;

public record ConsoleLine(long ts, String level, String text) {
    public static ConsoleLine info(String text)  { return new ConsoleLine(System.currentTimeMillis(), "INFO", text); }
    public static ConsoleLine out(String text)   { return new ConsoleLine(System.currentTimeMillis(), "OUT", text); }
    public static ConsoleLine error(String text) { return new ConsoleLine(System.currentTimeMillis(), "ERROR", text); }
}
