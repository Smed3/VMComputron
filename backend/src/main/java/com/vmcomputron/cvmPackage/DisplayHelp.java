//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

import java.awt.Desktop;
import java.io.File;

class DisplayHelp {
    public DisplayHelp() {
        try {
            File pdfFile = new File("help/CvmHelp.pdf");
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }
            } else {
                System.out.println("File does not exist!");
            }
        } catch (Exception var2) {
        }

    }
}
