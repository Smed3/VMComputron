//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

public class PDNumeric extends CvmRegisters {
    public PDNumeric() {
    }

    static void sendRValue(Computron computron, float newREGvalue) {
        computron.switchButton(regSwitch, false);
        R = newREGvalue;
        RL = CvmFloatTransform.lowShortOf(R);
        RH = CvmFloatTransform.highShortOf(R);
        computron.displayRH(RH);
        computron.displayRL(RL);
        computron.displayR(R);
        computron.displayREGButtons(RL);
        regSwitch = Switch.selRL;
        computron.switchButton(regSwitch, true);
        REG = RL;
    }

    static void sendREGValue(Computron computron, int newREGValue, CvmRegisters.Switch newREGSwitch) {
        computron.switchButton(regSwitch, false);
        switch (newREGSwitch) {
            case selPC:
                PC = newREGValue;
                computron.displayPC(PC);
                sendMValue(computron, M[PC]);
                computron.displayM(M[PC]);
                break;
            case selA:
                A = newREGValue;
                computron.displayA(A);
                break;
            case selX:
                X = newREGValue;
                computron.displayX(X);
                break;
            case selSP:
                SP = newREGValue;
                computron.displaySP(SP);
                break;
            case selRH:
                RH = newREGValue;
                R = CvmFloatTransform.floatFrom(RL, RH);
                computron.displayRH(RH);
                computron.displayR(R);
                break;
            case selRL:
                RL = newREGValue;
                R = CvmFloatTransform.floatFrom(RL, RH);
                computron.displayRL(RL);
                computron.displayR(R);
        }

        computron.displayREGButtons(newREGValue);
        regSwitch = newREGSwitch;
        computron.switchButton(regSwitch, true);
        REG = newREGValue;
    }

    static void sendMValue(Computron computron, int newIntValue) {
        computron.displayMEMButtons(newIntValue);
        computron.displayM(newIntValue);
        MEM = newIntValue;
        M[PC] = MEM;
        if (PC < 65535) {
            computron.displayMR(CvmFloatTransform.floatFrom(M[PC], M[PC + 1]));
        } else {
            computron.displayMR(Float.POSITIVE_INFINITY);
        }

    }

    static void sendMRValue(Computron computron, float newFloatvalue) {
        M[PC] = CvmFloatTransform.lowShortOf(newFloatvalue);
        computron.displayMEMButtons(M[PC]);
        MEM = M[PC];
        computron.displayMEMButtons(MEM);
        computron.displayM(M[PC]);
        if (PC < 65535) {
            M[PC + 1] = CvmFloatTransform.highShortOf(newFloatvalue);
            computron.displayMR(CvmFloatTransform.floatFrom(M[PC], M[PC + 1]));
        } else {
            computron.displayMR(Float.POSITIVE_INFINITY);
        }

    }
}
