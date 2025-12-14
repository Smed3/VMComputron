package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.ConsoleLine;
import com.vmcomputron.model.VmStateResponse;

class VmControllerHelper {
    VmStateResponse state(boolean ok, boolean needsInput, ConsoleLine lastLine) {
        int pc = CvmRegisters.getPC();
        return new VmStateResponse(
                ok,
                CvmRegisters.isRunning(),
                needsInput,
                null,
                pc,
                CvmRegisters.getSP(),
                CvmRegisters.getA(),
                CvmRegisters.getX(),
                CvmRegisters.getR(),
                CvmRegisters.getRH(),
                CvmRegisters.getRL(),
                CvmRegisters.getM(pc),
                lastLine
        );
    }

    VmStateResponse fail(String err) {
        int pc = CvmRegisters.getPC();
        return new VmStateResponse(
                false,
                CvmRegisters.isRunning(),
                false,
                err,
                pc,
                CvmRegisters.getSP(),
                CvmRegisters.getA(),
                CvmRegisters.getX(),
                CvmRegisters.getR(),
                CvmRegisters.getRH(),
                CvmRegisters.getRL(),
                CvmRegisters.getM(pc),
                null
        );
    }
}
