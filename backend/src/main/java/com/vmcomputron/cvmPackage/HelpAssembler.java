//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

public class HelpAssembler {
    private final String[] instrName = new String[]{"NOP", "BZE adr", "JMP adr", "JSR adr", "RTS", "EXIT", "INPC", "INP", "INPR", "OUTC", "OUT", "OUTR", "POP", "POPR", "PUSH", "PUSHR", "LDA adr", "LDA’M val", "LDA’I adr", "LDA’X val", "LDR adr", "LDR’I adr", "STA adr", "STA’I adr", "STR’I adr", "LDX adr", "STX adr", "LDS adr", "STS adr", "OR adr", "AND adr", "NOT", "EQ adr", "NE adr", "LT adr", "LE adr", "GT adr", "GE adr", "EQR adr", "NER adr", "LTR adr", "LER adr", "GTR adr", "GER adr", "ADD adr", "ADD’M val", "SUB adr", "SUB’M val", "MUL adr", "DIV adr", "NEG", "ADDR adr", "SUBR adr", "MULR adr", "DIVR adr", "NEGR"};

    public HelpAssembler() {
    }

    public void getHelpText(Computron help) {
        for(int code = 0; code < this.instrName.length; ++code) {
            help.setCode(code, code);
            int spIndex = this.instrName[code].indexOf(" ");
            if (spIndex < 0) {
                help.setInstr(code, this.instrName[code]);
            } else {
                help.setInstr(code, this.instrName[code].substring(0, spIndex));
                help.setAttr(code, this.instrName[code].substring(spIndex + 1));
            }
        }

    }
}
