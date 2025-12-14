package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.*;
import com.vmcomputron.service.ConsoleService;
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
    // Client -> /app/hello
    // Server -> /topic/greetings
    // ==========================
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting hello(String message) {
        return new Greeting("Hello from server: " + message);
    }

    // ==========================
    // 1) REGISTERS UPDATE (from UI)
    // Client -> /app/registerUpdated  { register: "A", newValue: 123 }
    // Server pushes:
    //   /topic/register/A (single register)
    //   /topic/memory     (M[PC] for sync)
    // ==========================
    @MessageMapping("/registerUpdated")
    public void handleRegisterUpdate(@Payload RegisterUpdateRequest request) {
        CvmRegisters.updateRegister(request.register(), request.newValue());

        String reg = request.register().toUpperCase();

        switch (reg) {
            case "PC" -> messaging.convertAndSend("/topic/register/PC", Register.pc(CvmRegisters.getPC()));
            case "SP" -> messaging.convertAndSend("/topic/register/SP", Register.sp(CvmRegisters.getSP()));
            case "A"  -> messaging.convertAndSend("/topic/register/A",  Register.a(CvmRegisters.getA()));
            case "X"  -> messaging.convertAndSend("/topic/register/X",  Register.x(CvmRegisters.getX()));
            case "RH" -> messaging.convertAndSend("/topic/register/RH", Register.rh(CvmRegisters.getRH()));
            case "RL" -> messaging.convertAndSend("/topic/register/RL", Register.rl(CvmRegisters.getRL()));
            default -> throw new IllegalArgumentException("Unknown register: " + reg);
        }

        // sync current memory cell at PC
        messaging.convertAndSend("/topic/memory", Register.m(CvmRegisters.getM(CvmRegisters.getPC())));
    }

    // ==========================
    // 2) MEMORY UPDATE (from UI)
    // Client -> /app/memoryUpdated { newValue: 123 }
    // Server -> /topic/memory (updated M[PC])
    // ==========================
    @MessageMapping("/memoryUpdated")
    @SendTo("/topic/memory")
    public Register handleMemoryUpdate(@Payload MemoryUpdateRequest request) {
        int pc = CvmRegisters.getPC();
        CvmRegisters.setM(pc, request.newValue());
        return Register.m(CvmRegisters.getM(pc));
    }

    // ==========================
    // 3) LOAD: M[PC] -> selected register
    // Client -> /app/load { selectedRegister: "A" }
    // Server -> /topic/register/A (new value)
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
            default -> throw new IllegalArgumentException("Unsupported register: " + reg);
        }

        Register response = switch (reg) {
            case "PC" -> Register.pc(memValue);
            case "SP" -> Register.sp(memValue);
            case "A"  -> Register.a(memValue);
            case "X"  -> Register.x(memValue);
            case "RH" -> Register.rh(memValue);
            case "RL" -> Register.rl(memValue);
            default -> throw new IllegalArgumentException("Unsupported register: " + reg);
        };

        messaging.convertAndSend("/topic/register/" + reg, response);
    }

    // ==========================
    // 4) STORE: selected register -> M[PC]
    // Client -> /app/store { selectedRegister: "A" }
    // Server -> /topic/memory (new M[PC])
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
            default -> throw new IllegalArgumentException("Unsupported register: " + reg);
        };

        CvmRegisters.setM(CvmRegisters.getPC(), regValue);

        int memValue = CvmRegisters.getM(CvmRegisters.getPC());
        messaging.convertAndSend("/topic/memory", Register.m(memValue));
    }

    // ==========================
    // 5) CONSOLE
    //
    // Streaming new lines:
    //   server pushes each new ConsoleLine to /topic/console
    //   (done inside ConsoleService.append)
    //
    // Optional: get last N lines via WS:
    // Client -> /app/console/tail { n: 50 }
    // Server -> /topic/console/tail (List<ConsoleLine>)
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
        // можно пушнуть инфо строку в поток
        console.append(ConsoleLine.info("Console cleared"));
    }


    // ==========================
// 6) WS SELF-TEST
// Client -> /app/ws/ping
// Server -> /topic/ws/pong
// ==========================
    @MessageMapping("/ws/ping")
    public void wsPing() {
        messaging.convertAndSend("/topic/ws/pong",
                ConsoleLine.info("pong " + System.currentTimeMillis()));
    }

    @MessageMapping("memoryUpdated")
    @SendTo("/ram")
    public MemoryGridResponse getFullMemory() {
        String[][] grid = new String[64][3];

        int pc = 0; // Если нужно начинать с 0, или можно с CvmRegisters.getPC() и идти дальше
        int rowIndex = 0;

        for (int addr = 0; addr < 64 && rowIndex < 64; addr++) {
            // Проверяем, валидное ли значение (как у тебя было)
            int currentAddr = addr & 0xFFFF;
            long value = CvmRegisters.getM(currentAddr);

            if (!LoadStoreRequest.isValue((int)value) && addr != 0) {
                // Пропускаем недопустимые значения — оставляем пустую строку "00 00 00"
                grid[rowIndex][0] = "00";
                grid[rowIndex][1] = "00";
                grid[rowIndex][2] = "00";
            } else {
                byte b0 = (byte) ((value >> 16) & 0xFF);
                byte b1 = (byte) ((value >> 8)  & 0xFF);
                byte b2 = (byte) (value & 0xFF);

                grid[rowIndex][0] = String.format("%02X", b0 & 0xFF);
                grid[rowIndex][1] = String.format("%02X", b1 & 0xFF);
                grid[rowIndex][2] = String.format("%02X", b2 & 0xFF);
            }
            rowIndex++;
        }

        // Заполняем недостающие строки (на всякий случай)
        while (rowIndex < 64) {
            grid[rowIndex][0] = "00";
            grid[rowIndex][1] = "00";
            grid[rowIndex][2] = "00";
            rowIndex++;
        }

        // Делим на 4 части по 16 строк
        String[][] part1 = Arrays.copyOfRange(grid, 0,  16);
        String[][] part2 = Arrays.copyOfRange(grid, 16, 32);
        String[][] part3 = Arrays.copyOfRange(grid, 32, 48);
        String[][] part4 = Arrays.copyOfRange(grid, 48, 64);

        return new MemoryGridResponse(part1, part2, part3, part4);
    }

    // ==========================
// 7) CONSOLE TEST (forces a line into /topic/console)
// Client -> /app/console/test
// Server -> /topic/console  (because ConsoleService.append() pushes there)
// ==========================
    @MessageMapping("/console/test")
    public void consoleTest() {
        console.append(ConsoleLine.out("WS console test " + System.currentTimeMillis()));
    }
}
