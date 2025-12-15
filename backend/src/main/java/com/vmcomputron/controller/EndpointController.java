package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EndpointController {

    @GetMapping("/memory")
    public Map<String, String[][]> getMemory() {
        String[][] grid = new String[64][3];

        for (int addr = 0; addr < 64; addr++) {
            int value = CvmRegisters.getM(addr) & 0xFFFF;

            int b0 = (value >> 16) & 0xFF;
            int b1 = (value >> 8) & 0xFF;
            int b2 = value & 0xFF;

            grid[addr][0] = String.format("%02X", b0);
            grid[addr][1] = String.format("%02X", b1);
            grid[addr][2] = String.format("%02X", b2);
        }

        Map<String, String[][]> result = new LinkedHashMap<>();
        result.put("part1", Arrays.copyOfRange(grid, 0, 16));
        result.put("part2", Arrays.copyOfRange(grid, 16, 32));
        result.put("part3", Arrays.copyOfRange(grid, 32, 48));
        result.put("part4", Arrays.copyOfRange(grid, 48, 64));
        return result;
    }
}
