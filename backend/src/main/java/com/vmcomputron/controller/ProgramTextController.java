package com.vmcomputron.controller;

import com.vmcomputron.model.ConsoleLine;
import com.vmcomputron.model.ProgramParseResponse;
import com.vmcomputron.model.ProgramTextRequest;
import com.vmcomputron.model.VmStateResponse;
import com.vmcomputron.service.ProgramFileService;
import com.vmcomputron.service.VmService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/program")
public class ProgramTextController {

    private final ProgramFileService programFileService;
    private final VmService vmService;
    private final VmControllerHelper helper;

    public ProgramTextController(ProgramFileService programFileService, VmService vmService) {
        this.programFileService = programFileService;
        this.vmService = vmService;
        this.helper = new VmControllerHelper();
    }

    @PostMapping("/text")
    public ProgramParseResponse uploadText(@RequestBody ProgramTextRequest req) {
        return programFileService.saveTextAndParse(req.filename(), req.code());
    }

    @PostMapping("/text/run")
    public VmStateResponse uploadTextAndRun(@RequestBody ProgramTextRequest req,
                                            @RequestParam(defaultValue = "10000") int stepLimit) {

        var parse = programFileService.saveTextAndParse(req.filename(), req.code());
        if (!parse.ok()) {
            return helper.fail(parse.errors().isEmpty() ? "Parse failed" : parse.errors().get(0));
        }

        ConsoleLine last = null;

        for (int i = 0; i < stepLimit; i++) {
            var r = vmService.step();

            if (r.line() != null) last = r.line();

            if (r.needsInput()) return helper.state(true, true, last);
            if (!r.running())   return helper.state(true, false, last);
        }

        return helper.fail("Step limit reached");
    }
}
