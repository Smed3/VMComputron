package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.*;
import com.vmcomputron.service.ConsoleService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messaging;
    private final ConsoleService console;

    public WebSocketController(SimpMessagingTemplate messaging, ConsoleService console) {
        this.messaging = messaging;
        this.console = console;
    }

    // ==========================
    // 0) TEST / HELLO
    // ==========================
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting hello(String message) {
        return new Greeting("Hello from server: " + message);
    }

    // ==========================
    // 1) REGISTERS UPDATE (from UI)
    // ==========================
    @MessageMapping("/registerUpdated")
    public void handleRegisterUpdate(@Payload RegisterUpdateRequest request) {
        // Просто меняем регистр — событие RegisterChangedEvent полетит из CvmRegisters автоматически
        CvmRegisters.updateRegister(request.register(), request.newValue());
        // Ничего больше не делаем! Обновление UI произойдёт через @EventListener ниже
    }

    // ==========================
    // 2) MEMORY UPDATE (from UI)
    // ==========================
    @MessageMapping("/memoryUpdated")
    @SendTo("/topic/memory")
    public Register handleMemoryUpdate(@Payload MemoryUpdateRequest request) {
        int pc = CvmRegisters.getPC();
        CvmRegisters.setM(pc, request.newValue());
        // Событие MemoryCellChangedEvent полетит автоматически
        return Register.m(CvmRegisters.getM(pc));
    }

    // ==========================
    // 3) LOAD: M[PC] -> selected register
    // ==========================
    @MessageMapping("/load")
    public void handleLoad(@Payload LoadStoreRequest request) {
        String reg = request.getSelectedRegister().toUpperCase();
        int memValue = CvmRegisters.getM(CvmRegisters.getPC());

        switch (reg) {
            case "PC" -> CvmRegisters.setPC(memValue);
            case "SP" -> CvmRegisters.setSP(memValue);
            case "A"  -> CvmRegisters.setA(memValue);
            case "X"  -> CvmRegisters.setX(memValue);
            case "RH" -> CvmRegisters.setRH(memValue);
            case "RL" -> CvmRegisters.setRL(memValue);
            default   -> throw new IllegalArgumentException("Unsupported register: " + reg);
        }

        Register response = switch (reg) {
            case "PC" -> Register.pc(memValue);
            case "SP" -> Register.sp(memValue);
            case "A"  -> Register.a(memValue);
            case "X"  -> Register.x(memValue);
            case "RH" -> Register.rh(memValue);
            case "RL" -> Register.rl(memValue);
            default   -> throw new IllegalArgumentException("Unsupported register: " + reg);
        };

        // Отправляем только конкретный регистр (остальное — через события)
        messaging.convertAndSend("/topic/register/" + reg, response);
    }

    // ==========================
    // 4) STORE: selected register -> M[PC]
    // ==========================
    @MessageMapping("/store")
    public void handleStore(@Payload LoadStoreRequest request) {
        String reg = request.getSelectedRegister().toUpperCase();

        int regValue = switch (reg) {
            case "PC" -> CvmRegisters.getPC();
            case "SP" -> CvmRegisters.getSP();
            case "A"  -> CvmRegisters.getA();
            case "X"  -> CvmRegisters.getX();
            case "RH" -> CvmRegisters.getRH();
            case "RL" -> CvmRegisters.getRL();
            default   -> throw new IllegalArgumentException("Unsupported register: " + reg);
        };

        CvmRegisters.setM(CvmRegisters.getPC(), regValue);
        // Событие MemoryCellChangedEvent полетит автоматически
        // Текущая ячейка обновится через @EventListener
    }

    // ==========================
    // 5) CONSOLE
    // ==========================
    public record ConsoleTailRequest(Integer n) {}

    @MessageMapping("/console/tail")
    public void consoleTail(@Payload ConsoleTailRequest req) {
        int n = (req == null || req.n() == null) ? 50 : req.n();
        List<ConsoleLine> lines = console.tail(n);
        messaging.convertAndSend("/topic/console/tail", lines);
    }

    @MessageMapping("/console/clear")
    public void consoleClear() {
        console.clear();
        console.append(ConsoleLine.info("Console cleared"));
    }

    // ==========================
    // 6) WS SELF-TEST
    // ==========================
    @MessageMapping("/ws/ping")
    public void wsPing() {
        messaging.convertAndSend("/topic/ws/pong",
                ConsoleLine.info("pong " + System.currentTimeMillis()));
    }

    // ==========================
    // 7) FULL MEMORY GRID REQUEST (from client)
    // ==========================
    @MessageMapping("/memory")
    @SendTo("/topic/ram")
    public MemoryCellChangedEvent getFullMemory() {
        return buildFullMemoryGrid();
    }

    // ==========================
    // 8) CONSOLE TEST
    // ==========================
    @MessageMapping("/console/test")
    public void consoleTest() {
        console.append(ConsoleLine.out("WS console test " + System.currentTimeMillis()));
    }

    // ==================================================================
    // АВТОМАТИЧЕСКИЕ ОБНОВЛЕНИЯ ПО СОБЫТИЯМ ИЗ CvmRegisters
    // ==================================================================

    // ==================================================================
// АВТОМАТИЧЕСКИЕ ОБНОВЛЕНИЯ ПО СОБЫТИЯМ ИЗ CvmRegisters
// ==================================================================

    @EventListener
    public void onRegisterChanged(RegisterUpdateRequest event) {
        String reg = event.register().toUpperCase();  // геттер из record

        Register registerObj = switch (reg) {
            case "PC" -> Register.pc(event.newValue());
            case "SP" -> Register.sp(event.newValue());
            case "A"  -> Register.a(event.newValue());
            case "X"  -> Register.x(event.newValue());
            case "RH" -> Register.rh(event.newValue());
            case "RL" -> Register.rl(event.newValue());
            default   -> throw new IllegalStateException("Unexpected register: " + reg);
        };

        messaging.convertAndSend("/topic/register/" + reg, registerObj);
        messaging.convertAndSend("/topic/memory", Register.m(CvmRegisters.getM(CvmRegisters.getPC())));
    }

    @EventListener
    public void onMemoryCellChanged(MemoryUpdateRequest event) {
        messaging.convertAndSend("/topic/ram", buildFullMemoryGrid());

        if (event.address() == CvmRegisters.getPC()) {
            messaging.convertAndSend("/topic/memory", Register.m(event.newValue()));
        }
    }

    // ==================================================================
    // Вспомогательный метод для построения полной памяти
    // ==================================================================
    private MemoryCellChangedEvent buildFullMemoryGrid() {
        String[][] grid = new String[64][3];
        int rowIndex = 0;

        for (int addr = 0; addr < 64 && rowIndex < 64; addr++) {
            int currentAddr = addr & 0xFFFF;
            long value = CvmRegisters.getM(currentAddr);

            if (!LoadStoreRequest.isValue((int) value) && addr != 0) {
                grid[rowIndex][0] = "00";
                grid[rowIndex][1] = "00";
                grid[rowIndex][2] = "00";
            } else {
                byte b0 = (byte) ((value >> 16) & 0xFF);
                byte b1 = (byte) ((value >> 8) & 0xFF);
                byte b2 = (byte) (value & 0xFF);

                grid[rowIndex][0] = String.format("%02X", b0 & 0xFF);
                grid[rowIndex][1] = String.format("%02X", b1 & 0xFF);
                grid[rowIndex][2] = String.format("%02X", b2 & 0xFF);
            }
            rowIndex++;
        }

        while (rowIndex < 64) {
            grid[rowIndex][0] = "00";
            grid[rowIndex][1] = "00";
            grid[rowIndex][2] = "00";
            rowIndex++;
        }

        String[][] part1 = Arrays.copyOfRange(grid, 0, 16);
        String[][] part2 = Arrays.copyOfRange(grid, 16, 32);
        String[][] part3 = Arrays.copyOfRange(grid, 32, 48);
        String[][] part4 = Arrays.copyOfRange(grid, 48, 64);

        return new MemoryCellChangedEvent(part1, part2, part3, part4);
    }
}