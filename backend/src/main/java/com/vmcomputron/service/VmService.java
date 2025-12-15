package com.vmcomputron.service;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.ConsoleLine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VmService {

    private final ConsoleService console;
    private final VmHistory history = new VmHistory();

    public VmService(ConsoleService console) {
        this.console = console;
    }

    public synchronized void reset() {
        for (int i = 0; i < 256; i++) CvmRegisters.setM(i, 0);

        CvmRegisters.setPC(0);
        CvmRegisters.setSP(0);
        CvmRegisters.setAFromVm(0);
        CvmRegisters.setX(0);
        CvmRegisters.setRH(0);
        CvmRegisters.setRL(0);
        CvmRegisters.setCpuError(0);
        CvmRegisters.setRunning(false);

        CvmRegisters.clearAValid();

        console.clear();
        history.clear();

        console.append(ConsoleLine.info("VM reset"));
    }

    public synchronized StepResult step() {
        int pc = CvmRegisters.getPC();
        int op = CvmRegisters.getM(pc) & 0xFFFF;

        final int OP_EXIT = 5;
        final int OP_INP  = 7;
        final int OP_OUT  = 10;
        final int OP_ADDM = 45;
        final int OP_NOP  = 0;


        if (op == OP_INP && !CvmRegisters.isAValid()) {
            CvmRegisters.setRunning(true);
            return StepResult.waitingInput();
        }


        var memWrites = new ArrayList<VmHistory.MemWrite>();
        var snapBefore = new VmHistory.Snapshot(
                CvmRegisters.getPC(),
                CvmRegisters.getSP(),
                CvmRegisters.getA(),
                CvmRegisters.getX(),
                CvmRegisters.getR(),
                CvmRegisters.getRH(),
                CvmRegisters.getRL(),
                CvmRegisters.isRunning(),
                CvmRegisters.getCpuError(),
                memWrites,
                null
        );

        CvmRegisters.setRunning(true);


        if (op == OP_EXIT) {
            CvmRegisters.setRunning(false);
            CvmRegisters.setPC(pc + 1);

            history.pushUndo(snapBefore);
            return StepResult.ok(null, false);
        }


        if (op == OP_INP) {
            CvmRegisters.setPC(pc + 1);
            CvmRegisters.clearAValid();

            history.pushUndo(snapBefore);
            return StepResult.ok(null, true);
        }


        if (op == OP_ADDM) {
            int imm = CvmRegisters.getM(pc + 1);
            int a = CvmRegisters.getA();
            int res = (a + imm) & 0xFFFF;

            CvmRegisters.setAFromVm(res);
            CvmRegisters.setPC(pc + 2);

            history.pushUndo(snapBefore);
            return StepResult.ok(null, true);
        }


        if (op == OP_OUT) {
            int a = CvmRegisters.getA();
            ConsoleLine line = ConsoleLine.out(String.valueOf(a));
            console.append(line);

            CvmRegisters.setPC(pc + 1);

            var snapWithLine = new VmHistory.Snapshot(
                    snapBefore.pc(), snapBefore.sp(), snapBefore.a(), snapBefore.x(),
                    snapBefore.r(), snapBefore.rh(), snapBefore.rl(),
                    snapBefore.running(), snapBefore.cpuError(),
                    snapBefore.memWrites(),
                    line
            );

            history.pushUndo(snapWithLine);
            return StepResult.ok(line, true);
        }


        if (op == OP_NOP) {
            CvmRegisters.setPC(pc + 1);
            history.pushUndo(snapBefore);
            return StepResult.ok(null, true);
        }

        throw new IllegalArgumentException("Unsupported opcode at PC=" + pc + ": " + op);
    }

    public synchronized boolean stepBack() {
        VmHistory.Snapshot s = history.popUndo();
        if (s == null) return false;

        VmHistory.Snapshot cur = new VmHistory.Snapshot(
                CvmRegisters.getPC(), CvmRegisters.getSP(), CvmRegisters.getA(), CvmRegisters.getX(),
                CvmRegisters.getR(), CvmRegisters.getRH(), CvmRegisters.getRL(),
                CvmRegisters.isRunning(), CvmRegisters.getCpuError(),
                List.of(),
                null
        );
        history.pushRedo(cur);

        for (VmHistory.MemWrite w : s.memWrites()) {
            CvmRegisters.setM(w.addr(), w.oldValue());
        }

        CvmRegisters.setPC(s.pc());
        CvmRegisters.setSP(s.sp());
        CvmRegisters.setAFromVm(s.a());
        CvmRegisters.setX(s.x());
        CvmRegisters.setR(s.r());
        CvmRegisters.setRH(s.rh());
        CvmRegisters.setRL(s.rl());
        CvmRegisters.setRunning(s.running());
        CvmRegisters.setCpuError(s.cpuError());

        CvmRegisters.clearAValid();

        console.append(ConsoleLine.info("Step back"));
        return true;
    }

    public synchronized boolean stepForward() {
        VmHistory.Snapshot s = history.popRedo();
        if (s == null) return false;

        CvmRegisters.setPC(s.pc());
        CvmRegisters.setSP(s.sp());
        CvmRegisters.setAFromVm(s.a());
        CvmRegisters.setX(s.x());
        CvmRegisters.setR(s.r());
        CvmRegisters.setRH(s.rh());
        CvmRegisters.setRL(s.rl());
        CvmRegisters.setRunning(s.running());
        CvmRegisters.setCpuError(s.cpuError());

        CvmRegisters.clearAValid();

        console.append(ConsoleLine.info("Step forward (state restore)"));
        return true;
    }

    public record StepResult(ConsoleLine line, boolean running, boolean needsInput) {
        public static StepResult ok(ConsoleLine line, boolean running) {
            return new StepResult(line, running, false);
        }
        public static StepResult waitingInput() {
            return new StepResult(null, true, true);
        }
    }
}