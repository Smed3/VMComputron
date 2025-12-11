package com.vmcomputron.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") //Путь для http запросов
public class RegisterController {
    //эндпоинты
    @GetMapping("/{type}/{address}")
    public ResponseEntity<?> getRegister(@PathVariable String type,
                                         @PathVariable int address) {
        // Ваша логика здесь
        return ResponseEntity.ok("Register: " + type + ", Address: " + address);
    }
}