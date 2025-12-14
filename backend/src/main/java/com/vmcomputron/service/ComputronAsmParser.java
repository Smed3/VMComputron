package com.vmcomputron.service;

import java.util.ArrayList;
import java.util.List;

public class ComputronAsmParser {

    // opcodes строго по enum opType в CvmALUnit
    // NOP=0, ... INP=7, OUT=10, EXIT=5, ADDM=45 ...
    private static int opcode(String mnemonic) {
        return switch (mnemonic) {
            case "NOP" -> 0;
            case "BZE" -> 1;
            case "JMP" -> 2;
            case "JSR" -> 3;
            case "RTS" -> 4;
            case "EXIT" -> 5;
            case "INPC" -> 6;
            case "INP" -> 7;
            case "INPR" -> 8;
            case "OUTC" -> 9;
            case "OUT" -> 10;
            case "OUTR" -> 11;
            case "POP" -> 12;
            case "POPR" -> 13;
            case "PUSH" -> 14;
            case "PUSHR" -> 15;
            case "LDA" -> 16;
            case "LDAM" -> 17;
            case "LDAI" -> 18;
            case "LDAX" -> 19;
            case "LDR" -> 20;
            case "LDRI" -> 21;
            case "STA" -> 22;
            case "STAI" -> 23;
            case "STRI" -> 24;
            case "LDX" -> 25;
            case "STX" -> 26;
            case "LDS" -> 27;
            case "STS" -> 28;
            case "OR" -> 29;
            case "AND" -> 30;
            case "NOT" -> 31;
            case "EQ" -> 32;
            case "NE" -> 33;
            case "LT" -> 34;
            case "LE" -> 35;
            case "GT" -> 36;
            case "GE" -> 37;
            case "EQR" -> 38;
            case "NER" -> 39;
            case "LTR" -> 40;
            case "LER" -> 41;
            case "GTR" -> 42;
            case "GER" -> 43;
            case "ADD" -> 44;
            case "ADDM" -> 45;
            case "SUB" -> 46;
            case "SUBM" -> 47;
            case "MUL" -> 48;
            case "DIV" -> 49;
            case "NEG" -> 50;
            case "ADDR" -> 51;
            case "SUBR" -> 52;
            case "MULR" -> 53;
            case "DIVR" -> 54;
            case "NEGR" -> 55;
            default -> throw new IllegalArgumentException("Unknown instruction: " + mnemonic);
        };
    }

    private static boolean needsOperand(String mnemonic) {
        // Всё, что в VM читает M[PC+1] как операнд
        return switch (mnemonic) {
            case "BZE", "JMP", "JSR",
                 "LDA", "LDAM", "LDAI", "LDAX",
                 "LDR", "LDRI",
                 "STA", "STAI", "STRI",
                 "LDX", "STX", "LDS", "STS",
                 "OR", "AND",
                 "EQ", "NE", "LT", "LE", "GT", "GE",
                 "EQR", "NER", "LTR", "LER", "GTR", "GER",
                 "ADD", "ADDM", "SUB", "SUBM", "MUL", "DIV",
                 "ADDR", "SUBR", "MULR", "DIVR"
                    -> true;
            default -> false;
        };
    }

    private static int parseNumber(String s) {
        s = s.trim();

        // hex, если вдруг встретится
        if (s.startsWith("0x") || s.startsWith("0X")) {
            return Integer.parseInt(s.substring(2), 16);
        }

        // если начинается с 0 и состоит только из 0..7 => это octal
        // (кроме "0")
        if (s.length() > 1 && s.charAt(0) == '0' && s.matches("[0-7]+")) {
            return Integer.parseInt(s, 8);
        }

        // иначе считаем десятичным
        return Integer.parseInt(s, 10);
    }


    public static List<Integer> parse(String rawText) {
        // убрать BOM (то самое "﻿" в preview)
        String text = rawText;
        if (!text.isEmpty() && text.charAt(0) == '\uFEFF') {
            text = text.substring(1);
        }

        String[] lines = text.split("\\R"); // любые переводы строки
        List<Integer> words = new ArrayList<>();

        for (int lineNo = 1; lineNo <= lines.length; lineNo++) {
            String line = lines[lineNo - 1].trim();
            if (line.isEmpty()) continue;

            // опционально: комментарии
            int c1 = line.indexOf("//");
            if (c1 >= 0) line = line.substring(0, c1).trim();
            int c2 = line.indexOf("#");
            if (c2 >= 0) line = line.substring(0, c2).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String mnemonic = parts[0].toUpperCase();

            int op = opcode(mnemonic);
            words.add(op);

            if (needsOperand(mnemonic)) {
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Line " + lineNo + ": missing operand for " + mnemonic);
                }
                int operand = parseNumber(parts[1]);
                // сейчас считаем, что десятичное
                words.add(operand & 0xFFFF);
            }
        }

        return words;
    }
}
