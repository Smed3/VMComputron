package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController { // websocket

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    //если надо чтото новое отправить надо как в дб создать файл поддержку на примере greeting
    // Клиент отправляет на /app/hello → сервер broadcast на /topic/greetings
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting handle(String message) {
        return new Greeting("Привет от сервера: " + message);
    }


    @MessageMapping("/registerUpdated")
    @SendTo("/topic/registers")
    public void handleRegisterUpdate(@Payload RegisterUpdateRequest request) {

        CvmRegisters.updateRegister(request.register(), request.newValue());

        switch (request.register()) {
            case "PC":
                messagingTemplate.convertAndSend("/topic/register/PC", Register.pc(CvmRegisters.getPC()));
                break;
            case "SP":
                messagingTemplate.convertAndSend("/topic/register/SP", Register.sp(CvmRegisters.getSP()));
                break;
            case "A":
                messagingTemplate.convertAndSend("/topic/register/A", Register.a(CvmRegisters.getA()));
                break;
            case "X":
                messagingTemplate.convertAndSend("/topic/register/X", Register.x(CvmRegisters.getX()));
                break;
            case "RH":
                messagingTemplate.convertAndSend("/topic/register/RH", Register.rh(CvmRegisters.getRH()));
                break;
            case "RL":
                messagingTemplate.convertAndSend("/topic/register/RL", Register.rl(CvmRegisters.getRL()));
                break;
            default:
                throw new IllegalArgumentException("Unknown register: " + request.register());
        }
        messagingTemplate.convertAndSend("/topic/memory", Register.m(CvmRegisters.getM(CvmRegisters.getPC())));
    }

    @MessageMapping("/memoryUpdated")
    @SendTo("/topic/memory")
    public Register handleMemoryUpdate(@Payload MemoryUpdateRequest request) {
        int currentPc = CvmRegisters.getPC();

        // Обновляем память по адресу PC
        CvmRegisters.setM(currentPc, request.newValue());

        // Возвращаем обновлённую ячейку памяти как Register.m(...)
        return Register.m(CvmRegisters.getM(currentPc));
    }

    //    client.publish({
//        destination: '/app/registerUpdated',
//                body: JSON.stringify(42),                    // ← значение
//                headers: { 'register': 'A' }                 // ← имя регистра
//    });
//    client.subscribe('/topic/registers', (msg) => {
//    const regs = JSON.parse(msg.body);
//        setRegisters({
//                pc: regs.pc.toString(16).padStart(4, '0').toUpperCase(),
//                sp: regs.sp,
//                a: regs.a,
//                x: regs.x,
//                r: regs.r.toFixed(2),
//                rh: regs.rh,
//                rl: regs.rl
//    });
//    });
    @MessageMapping("/load")
    public void handleLoad(@Payload LoadStoreRequest request) {
        String reg = request.getSelectedRegister().toUpperCase();
        int memValue = CvmRegisters.getM(CvmRegisters.getPC());

        // Загружаем значение из памяти в выбранный регистр
        switch (reg) {
            case "PC" -> CvmRegisters.setPC(memValue);
            case "SP" -> CvmRegisters.setSP(memValue);
            case "A"  -> CvmRegisters.setA(memValue);
            case "X"  -> CvmRegisters.setX(memValue);
            case "RH" -> CvmRegisters.setRH(memValue);
            case "RL" -> CvmRegisters.setRL(memValue);
            default -> throw new IllegalArgumentException("Unsupported register: " + reg);
        }

        // Отправляем обновлённый регистр на отдельный топик
        Register response = switch (reg) {
            case "PC" -> Register.pc(memValue);
            case "SP" -> Register.sp(memValue);
            case "A"  -> Register.a(memValue);
            case "X"  -> Register.x(memValue);
            case "RH" -> Register.rh(memValue);
            case "RL" -> Register.rl(memValue);
            default -> throw new IllegalArgumentException();
        };

        messagingTemplate.convertAndSend("/topic/register/" + reg, response);

        // Также отправляем новое значение M[PC] (оно не изменилось, но для синхронизации)
        //messagingTemplate.convertAndSend("/topic/memory", Register.m(memValue));
    }

    // ================== STORE ==================
    // Фронтенд отправляет: { "selectedRegister": "A" }
    @MessageMapping("/store")
    public void handleStore(@Payload LoadStoreRequest request) {
        String reg = request.getSelectedRegister().toUpperCase();

        // Получаем значение выбранного регистра
        int regValue = switch (reg) {
            case "PC" -> CvmRegisters.getPC();
            case "SP" -> CvmRegisters.getSP();
            case "A"  -> CvmRegisters.getA();
            case "X"  -> CvmRegisters.getX();
            case "RH" -> CvmRegisters.getRH();
            case "RL" -> CvmRegisters.getRL();
            default -> throw new IllegalArgumentException("Unsupported register: " + reg);
        };

        // Записываем в память
        CvmRegisters.setM(CvmRegisters.getPC(), regValue);

        int memValue = CvmRegisters.getM(CvmRegisters.getPC());

        // Отправляем только обновлённое значение M[PC] + лампочки
        messagingTemplate.convertAndSend("/topic/memory", Register.m(memValue));
    }

}

