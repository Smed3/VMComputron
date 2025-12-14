package com.vmcomputron.model;

public class Register {

    private final String register;   // "PC", "A", "SP", "X", "RH", "RL", "M"
    private final int newValue;
    private final int[] cpu;

    static final int[] CPU_BIT_VALUES = {
            32768, 16384, 8192, 4096, 2048, 1024, 512,
            256, 128, 64, 32, 16, 8, 4, 2, 1
    };

    public Register(String register, int newValue) {
        this.register = register.toUpperCase();
        this.newValue = newValue;

        // Зажигаем лампочки по значению
        this.cpu = new int[16];
        int remaining = Math.abs(newValue);
        for (int i = 0; i < 16; i++) {
            if (remaining >= CPU_BIT_VALUES[i]) {
                this.cpu[i] = 1;
                remaining -= CPU_BIT_VALUES[i];
            } else {
                this.cpu[i] = 0;
            }
        }
    }

    public String getRegister() { return register; }
    public int getNewValue() { return newValue; }
    public int[] getCpu() { return cpu; }

    // Фабричные методы
    public static Register pc(int value)  { return new Register("PC", value); }
    public static Register sp(int value)  { return new Register("SP", value); }
    public static Register a(int value)   { return new Register("A", value); }
    public static Register x(int value)   { return new Register("X", value); }
    public static Register rh(int value)  { return new Register("RH", value); }
    public static Register rl(int value)  { return new Register("RL", value); }
    public static Register m(int value)   { return new Register("M", value); }
}