package com.vmcomputron.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {

    @GetMapping("/status")
    public String status() {
        return "Backend работает! Время: " + java.time.LocalDateTime.now();
    }

    @PostMapping("/execute")
    public String execute(@RequestBody CommandRequest request) {
        return "Выполнено: " + request.getCommand();
    }

    // DTO
    static class CommandRequest {
        private String command;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }
}