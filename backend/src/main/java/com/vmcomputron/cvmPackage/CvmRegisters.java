//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

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

    public CvmRegisters() {
    }

    void CvmRegisters() {
        for(int k = 0; k < 65536; ++k) {
            M[k] = 0;
        }

    }

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

    static enum RegMem {
        selREG,
        selMEM;

        private RegMem() {
        }
    }

    static enum Switch {
        selPC,
        selSP,
        selA,
        selX,
        selRH,
        selRL;

        private Switch() {
        }
    }
}
