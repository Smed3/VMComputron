//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDSave extends CvmRegisters {
    private String outputFile;

    public PDSave(String outputFile) {
        this.outputFile = outputFile;
    }

    public void save(Computron computron) {
        File dataFile = new File(this.outputFile);

        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
            if (Computron.getBeginPC() <= Computron.getEndPC()) {
                CvmSwitching.selectRegister(computron, Switch.selPC, CvmRegisters.PC);

                for(PC = Computron.getBeginPC(); PC <= Computron.getEndPC(); ++PC) {
                    REG = PC;
                    computron.displayREGButtons(REG);
                    CvmDigital.displayAllRegisters(computron);
                    CvmDigital.displayMEM(computron, PC);
                    out.writeByte(CvmRegisters.M[PC] & 255);
                    out.writeByte(CvmRegisters.M[PC] / 256 & 255);
                }

                out.close();
                PC = Computron.getBeginPC();
                REG = PC;
                computron.displayREGButtons(REG);
                CvmDigital.displayAllRegisters(computron);
                CvmDigital.displayMEM(computron, PC);
            } else {
                System.out.println("Wrong Begin-> End Range\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Computron.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (EOFException var5) {
        } catch (IOException ex) {
            Logger.getLogger(Computron.class.getName()).log(Level.SEVERE, (String)null, ex);
        }

    }
}
