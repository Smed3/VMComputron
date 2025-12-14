package com.vmcomputron.service;

import com.vmcomputron.model.ConsoleLine;

import java.util.ArrayList;
import java.util.List;

public class VmHistory {

    public record MemWrite(int addr, int oldValue) {}

    public record Snapshot(
            int pc, int sp, int a, int x,
            float r, int rh, int rl,
            boolean running, int cpuError,
            List<MemWrite> memWrites,
            ConsoleLine producedLine // если OUT что-то напечатал на этом шаге
    ) {}

    private final List<Snapshot> undo = new ArrayList<>();
    private final List<Snapshot> redo = new ArrayList<>();

    public void clear() {
        undo.clear();
        redo.clear();
    }

    public void pushUndo(Snapshot s) {
        undo.add(s);
        redo.clear(); // если сделали новый шаг — redo история больше не валидна
    }

    public Snapshot popUndo() {
        if (undo.isEmpty()) return null;
        return undo.remove(undo.size() - 1);
    }

    public void pushRedo(Snapshot s) {
        redo.add(s);
    }

    public Snapshot popRedo() {
        if (redo.isEmpty()) return null;
        return redo.remove(redo.size() - 1);
    }

    public int undoSize() { return undo.size(); }
    public int redoSize() { return redo.size(); }
}
