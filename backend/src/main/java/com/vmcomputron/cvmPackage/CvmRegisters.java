package com.vmcomputron.cvmPackage;

import com.vmcomputron.model.MemoryCellChangedEvent;
import com.vmcomputron.model.RegisterChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CvmRegisters {
    static int cpuError = 0;
    static boolean running = false;
    static int REG = 0;
    static int MEM = 0;
    static Switch regSwitch;
    static int PC;
    static int SP;
    static int A;
    static int X;
    static float R;
    static int RH;
    static int RL;
    static int[] M;

    // Добавляем publisher для событий
    private static ApplicationEventPublisher eventPublisher;

    // Spring сам найдёт бин и заинжектит его
    public CvmRegisters(ApplicationEventPublisher eventPublisher) {
        CvmRegisters.eventPublisher = eventPublisher;
    }

    // Чтобы при старте приложения всё инициализировалось как раньше
    @PostConstruct
    public void init() {
        regSwitch = Switch.selPC;
        PC = 0;
        SP = 0;
        A = 0;
        X = 0;
        R = 0.0F;
        RH = 0;
        RL = 0;
        M = new int[65536];
    }

    public CvmRegisters(int pc, int sp, int a, int x, float r, int rh, int rl) {
        this.PC = pc;
        this.SP = sp;
        this.A = a;
        this.X = x;
        this.R = r;
        this.RH = rh;
        this.RL = rl;
    }

    public CvmRegisters() {
    }

    void CvmRegisters() {
        for(int k = 0; k < 65536; ++k) {
            M[k] = 0;
        }
    }

    // ========================
    // Твои геттеры — без изменений
    // ========================
    public static int getCpuError() {
        return cpuError;
    }

    public static void setCpuError(int value) {
        cpuError = value;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean value) {
        running = value;
    }

    public static int getREG() {
        return REG;
    }

    public static void setREG(int value) {
        REG = value;
    }

    public static int getMEM() {
        return MEM;
    }

    public static void setMEM(int value) {
        MEM = value;
    }

    public static Switch getRegSwitch() {
        return regSwitch;
    }

    public static void setRegSwitch(Switch value) {
        regSwitch = value;
    }

    public static int getPC() {
        return PC;
    }

    public static void setPC(int value) {
        PC = value;
        // Добавляем событие
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new RegisterChangedEvent("PC", value));
        }
    }

    public static int getSP() {
        return SP;
    }

    public static void setSP(int value) {
        SP = value;
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new RegisterChangedEvent("SP", value));
        }
    }

    public static int getA() {
        return A;
    }

    public static void setA(int value) {
        A = value;
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new RegisterChangedEvent("A", value));
        }
    }

    public static int getX() {
        return X;
    }

    public static void setX(int value) {
        X = value;
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new RegisterChangedEvent("X", value));
        }
    }

    public static float getR() {
        return R;
    }

    public static void setR(float value) {
        R = value;
    }

    public static int getRH() {
        return RH;
    }

    public static void setRH(int value) {
        RH = value;
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new RegisterChangedEvent("RH", value));
        }
    }

    public static int getRL() {
        return RL;
    }

    public static void setRL(int value) {
        RL = value;
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new RegisterChangedEvent("RL", value));
        }
    }

    public static int[] getM() {
        return M;
    }

    public static void setM(int[] values) {
        if (values != null && values.length == M.length) {
            System.arraycopy(values, 0, M, 0, values.length);
        }
    }

    public static int getM(int index) {
        if (index >= 0 && index < M.length) {
            return M[index];
        }
        return 0;
    }

    public static void setM(int index, int value) {
        if (index >= 0 && index < M.length) {
            M[index] = value;
            // Добавляем событие
            if (eventPublisher != null) {
                eventPublisher.publishEvent(new MemoryCellChangedEvent(index));
            }
        }
    }

    // Твоя статическая инициализация — оставляем как есть
    static {
        regSwitch = CvmRegisters.Switch.selPC;
        PC = 0;
        SP = 0;
        A = 0;
        X = 0;
        R = 0.0F;
        RH = 0;
        RL = 0;
        M = new int[65536];
    }

    public static enum RegMem {
        selREG,
        selMEM;

        private RegMem() {
        }
    }

    public static enum Switch {
        selPC,
        selSP,
        selA,
        selX,
        selRH,
        selRL;

        private Switch() {
        }
    }

    // Твой updateRegister — оставляем полностью как был,
    // но он теперь тоже вызывает сеттеры → события полетят автоматически!
    public static void updateRegister(String registerName, Integer value){
        switch(registerName.toUpperCase()){
            case "PC":
                setPC(value);     // ← событие полетит
                break;
            case "SP":
                setSP(value);     // ← событие полетит
                break;
            case "A":
                setA(value);      // ← событие полетит
                break;
            case "X":
                setX(value);      // ← событие полетит
                break;
            case "R":
                R = value;
                break;
            case "RH":
                setRH(value);     // ← событие полетит
                break;
            case "RL":
                setRL(value);     // ← событие полетит
                break;
            default :
                throw new IllegalArgumentException("Unknown register: " + registerName);
        }
    }

    public static CvmRegisters getCurrentState() {
        return new CvmRegisters(PC, SP, A, X, R, RH, RL);
    }
}