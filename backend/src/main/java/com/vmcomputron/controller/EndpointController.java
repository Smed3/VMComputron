package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.LoadStoreRequest;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

//        CvmRegisters.setM(1, 123);
//        CvmRegisters.setM(2, 11234);
//        CvmRegisters.setM(3, 123456);
@RestController
@RequestMapping("/api") //Путь для http запросов
public class EndpointController {
    //эндпоинты

    @GetMapping("/memory")
    public Map<String, String[][]> getMemory() {
        int pc = 0;
        String[][] grid = new String[64][3];
//        CvmRegisters.setM(1, 123);
//        CvmRegisters.setM(2, 11234);
//        CvmRegisters.setM(3, 456123);
//        CvmRegisters.setM(4, 123456);
//        CvmRegisters.setM(5, 123456);
//        CvmRegisters.setM(6, 123456);
//        CvmRegisters.setM(7, 123456);
//        CvmRegisters.setM(8, 123456);
        for (int row = 0; row < 64; row++) {

            if (!LoadStoreRequest.isValue(pc) && pc!=0){
                row--;
                //System.out.println("Invalid cifre: "+pc);
                pc+=1;

                continue;
            }
            int addr = (pc) & 0xFFFF;
            long value = CvmRegisters.getM(addr);  // long, чтобы не обрезать

            // Заполняем всю строку "00"
            for (int col = 0; col < 3; col++) {
                grid[row][col] = "00";
            }

            // Разбиваем на байты: берём младшие 3 байта (24 бита) для 01 E2 40
            // Поскольку 123456 = 0x01E240, байты: 01 E2 40 (big-endian)
            byte b0 = (byte) ((value >> 16) & 0xFF);  // 01
            byte b1 = (byte) ((value >> 8) & 0xFF);   // E2
            byte b2 = (byte) (value & 0xFF);          // 40


            grid[row][0] = String.format("%02X", b0 & 0xFF);
            grid[row][1] = String.format("%02X", b1 & 0xFF);
            grid[row][2] = String.format("%02X", b2 & 0xFF);
            pc+=1;
        }

        String[][] part1 = new String[16][3];
        String[][] part2 = new String[16][3];
        String[][] part3 = new String[16][3];
        String[][] part4 = new String[16][3];

        for (int i = 0; i < 16; i++) {
            part1[i] = grid[i];
            part2[i] = grid[i + 16];
            part3[i] = grid[i + 32];
            part4[i] = grid[i + 48];
        }

        // Создаем Map для JSON
        Map<String, String[][]> result = new LinkedHashMap<>();
        result.put("part1", part1);
        result.put("part2", part2);
        result.put("part3", part3);
        result.put("part4", part4);

        return result;
    }
}