package com.vmcomputron.controller;

import com.vmcomputron.model.Greeting;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController { // websocket
    //если надо чтото новое отправить надо как в дб создать файл поддержку на примере greeting
    // Клиент отправляет на /app/hello → сервер broadcast на /topic/greetings
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting handle(String message) {
        return new Greeting("Привет от сервера: " + message);
    }
}

