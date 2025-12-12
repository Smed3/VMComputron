package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
//        CvmRegisters.setM(1, 123);
//        CvmRegisters.setM(2, 11234);
//        CvmRegisters.setM(3, 123456);
@RestController
@RequestMapping("/api") //Путь для http запросов
public class EndpointController {
    //эндпоинты

    @GetMapping("/memory")
    public String[][] getMemory() {
        int pc = CvmRegisters.getPC();  // всегда от PC
        String[][] grid = new String[16][16];

        for (int row = 0; row < 16; row++) {
            int addr = (pc + row) & 0xFFFF;
            long value = CvmRegisters.getM(addr);  // long, чтобы не обрезать

            // Заполняем всю строку "00"
            for (int col = 0; col < 16; col++) {
                grid[row][col] = "00";
            }

            // Разбиваем на байты: берём младшие 3 байта (24 бита) для 01 E2 40
            // Поскольку 123456 = 0x01E240, байты: 01 E2 40 (big-endian)
            byte b0 = (byte) ((value >> 16) & 0xFF);  // 01
            byte b1 = (byte) ((value >> 8) & 0xFF);   // E2
            byte b2 = (byte) (value & 0xFF);          // 40

            // Кладём в правые столбцы: столбец 13=01, 14=E2, 15=40
            grid[row][13] = String.format("%02X", b0 & 0xFF);
            grid[row][14] = String.format("%02X", b1 & 0xFF);
            grid[row][15] = String.format("%02X", b2 & 0xFF);
        }

        return grid;
    }
}