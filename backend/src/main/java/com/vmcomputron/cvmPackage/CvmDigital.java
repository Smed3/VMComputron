//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

public class CvmDigital extends CvmRegisters {
    public CvmDigital() {
    }

    static void toggleBit(Computron computron, CvmRegisters.RegMem selREGMEM, int n) {
        int nBit = 1 << n;
        switch (selREGMEM) {
            case selREG:
                if ((REG & nBit) == 0) {
                    REG += nBit;
                } else {
                    REG -= nBit;
                }

                displayREG(computron, REG, regSwitch);
                break;
            case selMEM:
                if ((MEM & nBit) == 0) {
                    MEM += nBit;
                } else {
                    MEM -= nBit;
                }

                M[PC] = MEM;
                displayMEM(computron, PC);
        }

    }

    static void displayREG(Computron computron, int regmemValue, CvmRegisters.Switch newREGSwitch) {
        REG = regmemValue;
        switch (newREGSwitch) {
            case selPC:
                PC = regmemValue;
                computron.displayPC(PC);
                displayMEM(computron, PC);
                break;
            case selSP:
                SP = regmemValue;
                computron.displaySP(SP);
                break;
            case selA:
                A = regmemValue;
                computron.displayA(A);
                break;
            case selX:
                X = regmemValue;
                computron.displayX(X);
                break;
            case selRH:
                RH = regmemValue;
                R = CvmFloatTransform.floatFrom(RL, RH);
                computron.displayRH(RH);
                computron.displayR(R);
                break;
            case selRL:
                RL = regmemValue;
                R = CvmFloatTransform.floatFrom(RL, RH);
                computron.displayRL(RL);
                computron.displayR(R);
        }

        computron.displayREGButtons(regmemValue);
    }

    static void displayAllRegisters(Computron computron) {
        computron.displayPC(PC);
        computron.displaySP(SP);
        computron.displayA(A);
        computron.displayX(X);
        computron.displayRL(RL);
        computron.displayRH(RH);
        computron.displayR(R);
    }

    public static void displayMEM(Computron computron, int PC) {
        MEM = M[PC];
        computron.displayMEMButtons(MEM);
        computron.displayM(M[PC]);
        if (PC < 65535) {
            computron.displayMR(CvmFloatTransform.floatFrom(M[PC], M[PC + 1]));
        } else {
            computron.displayMR(Float.POSITIVE_INFINITY);
        }

    }
}
