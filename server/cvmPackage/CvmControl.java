//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cvmPackage;

import cvmPackage.CvmRegisters.Switch;

public class CvmControl extends CvmRegisters {
    public CvmControl() {
    }

    public static void incrDecrREG(Computron computron, int increment) {
        if (PC > 0 && increment < 0 || PC < 65535 && increment > 0) {
            PC += increment;
            if (regSwitch == Switch.selPC) {
                REG = PC;
                CvmDigital.displayREG(computron, REG, regSwitch);
            } else {
                computron.displayPC(PC);
                CvmDigital.displayMEM(computron, PC);
            }
        }

    }

    public static void loadREG(Computron computron) {
        REG = M[PC];
        CvmDigital.displayREG(computron, REG, regSwitch);
    }

    public static void storeREG(Computron computron) {
        M[PC] = REG;
        CvmDigital.displayMEM(computron, PC);
    }

    public static void execStep(Computron computron) {
        CvmALUnit.exeInstruction(computron);
        switch (regSwitch) {
            case selPC:
                REG = PC;
                break;
            case selSP:
                REG = SP;
                break;
            case selA:
                REG = A;
                break;
            case selX:
                REG = X;
                break;
            case selRH:
                REG = RH;
                break;
            case selRL:
                REG = RL;
        }

        computron.displayREGButtons(REG);
        CvmDigital.displayAllRegisters(computron);
        CvmDigital.displayMEM(computron, PC);
    }

    public static void execComputron(Computron computron) {
        while(running) {
            execStep(computron);
        }

        computron.switchOffrunButton();
    }
}
