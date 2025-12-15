package com.vmcomputron.controller;

import com.vmcomputron.model.ConsoleLine;
import com.vmcomputron.model.ProgramParseResponse;
import com.vmcomputron.service.ConsoleService;
import com.vmcomputron.service.ProgramFileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProgramRAMController {

    private final ProgramFileService programFileService;
    private final ConsoleService console;

    public ProgramRAMController(ProgramFileService programFileService, ConsoleService console) {
        this.programFileService = programFileService;
        this.console = console;
    }

    @PostMapping(value = "/program/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProgramParseResponse uploadFile(@RequestPart("file") MultipartFile file) {
        console.append(ConsoleLine.info("Program upload started: " + safe(file.getOriginalFilename())));

        ProgramParseResponse resp = programFileService.saveAndParseViaPython(file);

        if (resp.ok()) {
            console.append(ConsoleLine.info("Saved to: " + safe(resp.savedPath())));
            console.append(ConsoleLine.info("Parsing: OK"));

            if (resp.parsed() != null) {
                console.append(ConsoleLine.out("Parsed result: " + resp.parsed()));
            } else {
                console.append(ConsoleLine.info("Parsed result: (empty)"));
            }
        } else {
            console.append(ConsoleLine.error("Parsing: FAILED"));
            if (resp.errors() != null) {
                for (String err : resp.errors()) {
                    console.append(ConsoleLine.error(err));
                }
            }
        }

        return resp;
    }

    private String safe(String s) {
        return s == null ? "(null)" : s.replaceAll("[\\r\\n\\t]", " ");
    }
}
