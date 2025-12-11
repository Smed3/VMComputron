package com.vmcomputron.model;

import com.vmcomputron.cvmPackage.CvmRegisters;
import org.springframework.stereotype.Component;

/**
 * DTO: сообщение об изменении ОДНОГО регистра + текущее состояние CPU-панели
 * Формат JSON:
 * {
 *   "register": "PC",
 *   "newValue": 4660,
 *   "cpu": [1,0,0,1,0,...]   // 16 бит, слева 1000000 → справа 1
 * }
 */

public class Register {

    private final String register;   // например "PC", "A", "SP"
    private final int newValue;      // новое значение регистра
    private final int[] cpu;         // 16 лампочек CPU

    // === Глобальная битовая маска CPU-флагов ===
    private static int cpuStatusFlags = 0;

    // === Порядок лампочек слева → справа: 1000000 → 1 ===
    private static final int[] CPU_BIT_VALUES = {
            1000000, 400000, 200000, 100000, 4000, 2000, 1000,
            400, 200, 100, 40, 20, 10, 4, 2, 1
            // 15    14    13    12    11   10   9   8   7   6   5   4   3  2  1  0
    };

    // === Конструктор: принимает имя регистра и новое значение ===
    public Register(String register, int newValue) {
        this.register = register.toUpperCase();
        this.newValue = newValue;

        // Автоматически формируем массив cpu[] в правильном порядке
        this.cpu = new int[16];
        for (int i = 0; i < 16; i++) {
            int bitIndex = 15 - i;  // i=0 → бит 15 (самый левый), i=15 → бит 0 (самый правый)
            this.cpu[i] = (newValue & (1 << bitIndex)) != 0 ? 1 : 0;
        }
    }

    // === Геттеры для Jackson (сериализация в JSON) ===
    public String getRegister() { return register; }
    public int getNewValue() { return newValue; }
    public int[] getCpu() { return cpu; }

    // === Управление CPU-лампочками (вызывай из эмулятора) ===
    public static void setCpuBit(int visualIndex, boolean on) {
        if (visualIndex < 0 || visualIndex > 15) {
            throw new IllegalArgumentException("CPU bit index must be 0–15");
        }
        int bit = 15 - visualIndex;
        if (on) {
            cpuStatusFlags |= (1 << bit);
        } else {
            cpuStatusFlags &= ~(1 << bit);
        }
    }

    public static void clearCpuPanel() {
        cpuStatusFlags = 0;
    }

    // === Удобные фабричные методы ===
    public static Register pc(int value) {
        return new Register("PC", value);
    }

    public static Register sp(int value) {
        return new Register("SP", value);
    }

    public static Register a(int value) {
        return new Register("A", value);
    }

    public static Register x(int value) {
        return new Register("X", value);
    }

    public static Register rh(int value) {
        return new Register("RH", value);
    }

    public static Register rl(int value) {
        return new Register("RL", value);
    }

    // Для float R — отдельный метод
    public static Register r(float value) {
        return new Register("R", (int) value); // или можно сделать String, если нужно
    }
}