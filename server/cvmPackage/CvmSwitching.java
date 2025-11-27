//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cvmPackage;

public class CvmSwitching extends CvmRegisters {
    public CvmSwitching() {
    }

    static void selectRegister(Computron computron, CvmRegisters.Switch newREGSwitch, int newREGValue) {
        computron.switchButton(regSwitch, false);
        computron.switchButton(newREGSwitch, true);
        regSwitch = newREGSwitch;
        switch (newREGSwitch) {
            case selPC:
                REG = PC;
                MEM = M[PC];
                computron.displayMEMButtons(MEM);
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
    }
}
