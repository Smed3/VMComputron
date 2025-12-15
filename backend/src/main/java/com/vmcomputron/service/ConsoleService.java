package com.vmcomputron.service;

import com.vmcomputron.model.ConsoleLine;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
public class ConsoleService {
    private static final int MAX_LINES = 1000;

    private final Deque<ConsoleLine> buffer = new ArrayDeque<>();
    private final SimpMessagingTemplate messaging;

    public ConsoleService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public synchronized void append(ConsoleLine line) {
        buffer.addLast(line);
        while (buffer.size() > MAX_LINES) buffer.removeFirst();


        messaging.convertAndSend("/topic/console", line);
    }

    public synchronized List<ConsoleLine> tail(int n) {
        int size = buffer.size();
        int skip = Math.max(0, size - Math.max(1, n));
        List<ConsoleLine> res = new ArrayList<>(Math.min(n, size));

        int i = 0;
        for (ConsoleLine line : buffer) {
            if (i++ >= skip) res.add(line);
        }
        return res;
    }

    public synchronized void clear() {
        buffer.clear();
    }
}
