//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

public class CvmALUnit extends CvmRegisters {
    public CvmALUnit() {
    }

    private static int tocvmShort(int value) {
        return value & '\uffff';
    }

    private static int tocvmInt(int value) {
        return (value & '耀') != 0 ? value | Short.MIN_VALUE : value;
    }

    public static void exeInstruction(Computron computron) {
        opType[] opCode = CvmALUnit.opType.values();
        if (M[PC] <= CvmALUnit.opType.NEGR.ordinal()) {
            switch (opCode[M[PC]]) {
                case NOP:
                    ++PC;
                    break;
                case BZE:
                    if (A == 0) {
                        PC = M[PC + 1];
                    } else {
                        PC += 2;
                    }
                    break;
                case JMP:
                    PC = M[PC + 1];
                    break;
                case JSR:
                    M[SP] = PC + 2;
                    ++SP;
                    PC = M[PC + 1];
                    break;
                case RTS:
                    --SP;
                    PC = M[SP];
                    break;
                case EXIT:
                    running = false;
                    break;
                case INPC:
                    A = (int)computron.receiveCFromScreen();
                    ++PC;
                    break;
                case INP:
                    A = computron.receiveIFromScreen();
                    ++PC;
                    break;
                case INPR:
                    R = computron.receiveRFromScreen();
                    ++PC;
                    break;
                case OUTC:
                    computron.sendCToScreen((char)A);
                    ++PC;
                    break;
                case OUT:
                    computron.sendIToScreen(tocvmInt(A));
                    ++PC;
                    break;
                case OUTR:
                    computron.sendRToScreen(R);
                    ++PC;
                    break;
                case POP:
                    --SP;
                    A = M[SP];
                    ++PC;
                    break;
                case POPR:
                    SP -= 2;
                    R = CvmFloatTransform.floatFrom(M[SP], M[SP + 1]);
                    ++PC;
                    break;
                case PUSH:
                    M[SP] = A;
                    ++SP;
                    ++PC;
                    break;
                case PUSHR:
                    M[SP] = CvmFloatTransform.lowShortOf(R);
                    M[SP + 1] = CvmFloatTransform.highShortOf(R);
                    SP += 2;
                    ++PC;
                    break;
                case LDA:
                    A = M[M[PC + 1]];
                    PC += 2;
                    break;
                case LDAM:
                    A = M[PC + 1];
                    PC += 2;
                    break;
                case LDAI:
                    A = M[M[M[PC + 1]]];
                    PC += 2;
                    break;
                case LDAX:
                    A = M[M[PC + 1] + X];
                    PC += 2;
                    break;
                case LDR:
                    R = CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1]);
                    PC += 2;
                    break;
                case LDRI:
                    R = CvmFloatTransform.floatFrom(M[M[M[PC + 1]]], M[M[M[PC + 1]] + 1]);
                    PC += 2;
                    break;
                case STA:
                    M[M[PC + 1]] = A;
                    PC += 2;
                    break;
                case STAI:
                    M[M[M[PC + 1]]] = A;
                    PC += 2;
                    break;
                case STRI:
                    M[M[M[PC + 1]]] = CvmFloatTransform.lowShortOf(R);
                    M[M[M[PC + 1]] + 1] = CvmFloatTransform.highShortOf(R);
                    PC += 2;
                    break;
                case LDX:
                    X = M[M[PC + 1]];
                    PC += 2;
                    break;
                case STX:
                    M[M[PC + 1]] = X;
                    PC += 2;
                    break;
                case LDS:
                    SP = M[M[PC + 1]];
                    PC += 2;
                    break;
                case STS:
                    M[M[PC + 1]] = SP;
                    PC += 2;
                    break;
                case OR:
                    if (A == 0 && M[M[PC + 1]] == 0) {
                        A = 0;
                    } else {
                        A = 1;
                    }

                    PC += 2;
                    break;
                case AND:
                    if (A == 1 && M[M[PC + 1]] == 1) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case NOT:
                    if (A == 1) {
                        A = 0;
                    } else {
                        A = 1;
                    }

                    ++PC;
                    break;
                case EQ:
                    if (A == M[M[PC + 1]]) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case NE:
                    if (A != M[M[PC + 1]]) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case LT:
                    if (tocvmInt(A) < tocvmInt(M[M[PC + 1]])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case LE:
                    if (tocvmInt(A) <= tocvmInt(M[M[PC + 1]])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case GT:
                    if (tocvmInt(A) > tocvmInt(M[M[PC + 1]])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case GE:
                    if (tocvmInt(A) >= tocvmInt(M[M[PC + 1]])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case EQR:
                    if (R == CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case NER:
                    if (R != CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case LTR:
                    if (R < CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case LER:
                    if (R <= CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case GTR:
                    if (R > CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case GER:
                    if (R >= CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1])) {
                        A = 1;
                    } else {
                        A = 0;
                    }

                    PC += 2;
                    break;
                case ADD:
                    A = tocvmShort(tocvmInt(A) + tocvmInt(M[M[PC + 1]]));
                    PC += 2;
                    break;
                case ADDM:
                    A = tocvmShort(tocvmInt(A) + tocvmInt(M[PC + 1]));
                    PC += 2;
                    break;
                case SUB:
                    A = tocvmShort(tocvmInt(A) - tocvmInt(M[M[PC + 1]]));
                    PC += 2;
                    break;
                case SUBM:
                    A = tocvmShort(tocvmInt(A) - tocvmInt(M[PC + 1]));
                    PC += 2;
                    break;
                case MUL:
                    A = tocvmShort(tocvmInt(A) * tocvmInt(M[M[PC + 1]]));
                    PC += 2;
                    break;
                case DIV:
                    A = tocvmShort(tocvmInt(A) / tocvmInt(M[M[PC + 1]]));
                    PC += 2;
                    break;
                case NEG:
                    A = tocvmShort(-tocvmInt(A));
                    ++PC;
                    break;
                case ADDR:
                    R += CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1]);
                    PC += 2;
                    break;
                case SUBR:
                    R -= CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1]);
                    PC += 2;
                    break;
                case MULR:
                    R *= CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1]);
                    PC += 2;
                    break;
                case DIVR:
                    R /= CvmFloatTransform.floatFrom(M[M[PC + 1]], M[M[PC + 1] + 1]);
                    PC += 2;
                    break;
                case NEGR:
                    R = -R;
                    ++PC;
            }

            if (PC > 65535) {
                cpuError = 2;
                PC = 65535;
                running = false;
            }
        } else {
            cpuError = 1;
            running = false;
        }

    }

    static enum opType {
        NOP,
        BZE,
        JMP,
        JSR,
        RTS,
        EXIT,
        INPC,
        INP,
        INPR,
        OUTC,
        OUT,
        OUTR,
        POP,
        POPR,
        PUSH,
        PUSHR,
        LDA,
        LDAM,
        LDAI,
        LDAX,
        LDR,
        LDRI,
        STA,
        STAI,
        STRI,
        LDX,
        STX,
        LDS,
        STS,
        OR,
        AND,
        NOT,
        EQ,
        NE,
        LT,
        LE,
        GT,
        GE,
        EQR,
        NER,
        LTR,
        LER,
        GTR,
        GER,
        ADD,
        ADDM,
        SUB,
        SUBM,
        MUL,
        DIV,
        NEG,
        ADDR,
        SUBR,
        MULR,
        DIVR,
        NEGR;

        private opType() {
        }
    }
}
