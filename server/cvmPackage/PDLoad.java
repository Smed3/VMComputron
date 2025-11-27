//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cvmPackage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDLoad extends CvmRegisters {
    private String inputFile;

    public PDLoad(String inputFile) {
        this.inputFile = inputFile;
    }

    public void load(Computron computron) {
        PC = Computron.getBeginPC();
        REG = PC;
        computron.displayREGButtons(REG);
        CvmDigital.displayAllRegisters(computron);
        CvmDigital.displayMEM(computron, PC);
        File dataFile = new File(this.inputFile);

        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFile)));

            while(true) {
                M[PC] = in.readUnsignedByte() + in.readUnsignedByte() * 256;
                REG = PC;
                computron.displayREGButtons(REG);
                CvmDigital.displayAllRegisters(computron);
                CvmDigital.displayMEM(computron, PC);
                ++PC;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Computron.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (EOFException var5) {
        } catch (IOException ex) {
            Logger.getLogger(Computron.class.getName()).log(Level.SEVERE, (String)null, ex);
        }

        computron.setEndPC(PC - 1);
        PC = Computron.getBeginPC();
        REG = PC;
        computron.displayREGButtons(REG);
        CvmDigital.displayAllRegisters(computron);
        CvmDigital.displayMEM(computron, PC);
    }
}
