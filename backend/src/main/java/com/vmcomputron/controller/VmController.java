package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.ConsoleLine;
import com.vmcomputron.model.VmStateResponse;
import com.vmcomputron.model.VmStepRequest;
import com.vmcomputron.service.VmService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vm")
public class VmController {

    private final VmService vm;

    public VmController(VmService vm) {
        this.vm = vm;
    }

    @PostMapping("/reset")
    public VmStateResponse reset() {
        vm.reset();
        return state(true, null, false, null);
    }

    @PostMapping("/step")
    public VmStateResponse step(@RequestBody(required = false) VmStepRequest req) {
        try {
            var res = vm.step();
            return state(true, res.line(), res.needsInput(), null);
        } catch (Exception e) {
            return state(false, null, false, e.getMessage());
        }
    }

    @PostMapping("/run")
    public VmStateResponse run(@RequestBody(required = false) VmStepRequest req,
                               @RequestParam(defaultValue = "10000") int stepLimit) {
        try {
            ConsoleLine last = null;

            for (int i = 0; i < stepLimit; i++) {
                var r = vm.step();

                if (r.line() != null) last = r.line();
                if (!r.running()) break;
                if (r.needsInput()) return state(true, last, true, null);
            }

            return state(true, last, false, null);
        } catch (Exception e) {
            return state(false, null, false, e.getMessage());
        }
    }

    private VmStateResponse state(boolean ok, ConsoleLine lastLine, boolean needsInput, String error) {
        int pc = CvmRegisters.getPC();
        int mAtPc = CvmRegisters.getM(pc);

        return new VmStateResponse(
                ok,
                CvmRegisters.isRunning(),
                needsInput,
                error,
                pc,
                CvmRegisters.getSP(),
                CvmRegisters.getA(),
                CvmRegisters.getX(),
                CvmRegisters.getR(),
                CvmRegisters.getRH(),
                CvmRegisters.getRL(),
                mAtPc,
                lastLine
        );
    }

    @PostMapping("/back")
    public VmStateResponse back() {
        boolean ok = vm.stepBack();
        return state(ok, null, false, ok ? null : "Nothing to undo");
    }

    @PostMapping("/forward")
    public VmStateResponse forward() {
        boolean ok = vm.stepForward();
        return state(ok, null, false, ok ? null : "Nothing to redo");
    }
}
