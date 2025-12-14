package com.vmcomputron.controller;

import com.vmcomputron.model.ConsoleLine;
import com.vmcomputron.service.ConsoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/console")
public class ConsoleController {

    private final ConsoleService console;

    public ConsoleController(ConsoleService console) {
        this.console = console;
    }

    @GetMapping("/tail")
    public List<ConsoleLine> tail(@RequestParam(defaultValue = "50") int n) {
        return console.tail(n);
    }
}
