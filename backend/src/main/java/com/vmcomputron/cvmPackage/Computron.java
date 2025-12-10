package com.vmcomputron.cvmPackage;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import static com.vmcomputron.cvmPackage.CvmRegisters.*;


public class Computron extends JFrame {

    // === Поля для ввода-вывода ===
    private boolean charAcquired = false;
    private char typedChar;
    private String numberString = "";
    private int oldCaretPosition;
    private boolean numberFormed = false;

    private static int fromPCValue = 0;
    private static int toPCValue = 65535;

    // === UI Компоненты (объявлены, но не все инициализированы здесь ради краткости) ===
    private JTextArea screenText;
    private JScrollPane screenScrollPane;

    private JTextField dispPC, dispSP, dispA, dispX, dispRH, dispRL, dispR, dispM, dispMR;
    private JTextField fromPC, toPC;

    private JToggleButton[] regBitButtons = new JToggleButton[16];
    private JToggleButton[] memBitButtons = new JToggleButton[16];

    private JButton runButton;
    private JTable helpTable;
    private JScrollPane helpScrollPane;

    // === Конструктор ===
    public Computron() {
        initComponents();
        initScreen();
        initHelp();
        ProgramDeviceInit();
    }

    private void initComponents() {
        setTitle("Computron Virtual Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Экран ввода-вывода ===
        screenText = new JTextArea(10, 50);
        screenText.setFont(new Font("Monospaced", Font.PLAIN, 14));
        screenScrollPane = new JScrollPane(screenText);
        add(screenScrollPane, BorderLayout.SOUTH);

        // === Панель регистров ===
        JPanel regPanel = new JPanel(new GridLayout(9, 2));
        dispPC = new JTextField(8); dispPC.setEditable(false);
        dispSP = new JTextField(8); dispSP.setEditable(false);
        dispA  = new JTextField(8); dispA.setEditable(false);
        dispX  = new JTextField(8); dispX.setEditable(false);
        dispRH = new JTextField(8); dispRH.setEditable(false);
        dispRL = new JTextField(8); dispRL.setEditable(false);
        dispR  = new JTextField(12); dispR.setEditable(false);
        dispM  = new JTextField(8); dispM.setEditable(false);
        dispMR = new JTextField(12); dispMR.setEditable(false);

        regPanel.add(new JLabel("PC:")); regPanel.add(dispPC);
        regPanel.add(new JLabel("SP:")); regPanel.add(dispSP);
        regPanel.add(new JLabel("A:"));  regPanel.add(dispA);
        regPanel.add(new JLabel("X:"));  regPanel.add(dispX);
        regPanel.add(new JLabel("RH:")); regPanel.add(dispRH);
        regPanel.add(new JLabel("RL:")); regPanel.add(dispRL);
        regPanel.add(new JLabel("R:"));  regPanel.add(dispR);
        regPanel.add(new JLabel("M[PC]:")); regPanel.add(dispM);
        regPanel.add(new JLabel("MR:")); regPanel.add(dispMR);

        add(regPanel, BorderLayout.NORTH);

        // === Кнопки переключения регистров ===
        JPanel switchPanel = new JPanel();
        JButton pcBtn = new JButton("PC");
        pcBtn.addActionListener(e -> CvmSwitching.selectRegister(this, Switch.selPC, PC));
        JButton spBtn = new JButton("SP");
        spBtn.addActionListener(e -> CvmSwitching.selectRegister(this, Switch.selSP, SP));
        JButton aBtn = new JButton("A");
        aBtn.addActionListener(e -> CvmSwitching.selectRegister(this, Switch.selA, A));
        JButton xBtn = new JButton("X");
        xBtn.addActionListener(e -> CvmSwitching.selectRegister(this, Switch.selX, X));
        JButton rhBtn = new JButton("RH");
        rhBtn.addActionListener(e -> CvmSwitching.selectRegister(this, Switch.selRH, RH));
        JButton rlBtn = new JButton("RL");
        rlBtn.addActionListener(e -> CvmSwitching.selectRegister(this, Switch.selRL, RL));

        switchPanel.add(pcBtn); switchPanel.add(spBtn); switchPanel.add(aBtn);
        switchPanel.add(xBtn); switchPanel.add(rhBtn); switchPanel.add(rlBtn);

        // === Кнопка Run ===
        runButton = new JButton("RUN");
        runButton.addActionListener(e -> {
            running = true;
            new Thread(() -> CvmControl.execComputron(this)).start();
        });
        switchPanel.add(runButton);

        add(switchPanel, BorderLayout.CENTER);

        // === Битовые кнопки для REG и MEM (упрощённо) ===
        JPanel bitsPanel = new JPanel(new GridLayout(2, 16));
        for (int i = 15; i >= 0; i--) {
            final int bit = i;
            JToggleButton b = new JToggleButton(String.valueOf(bit));
            b.addActionListener(e -> CvmDigital.toggleBit(this, RegMem.selREG, bit));
            regBitButtons[bit] = b;
            bitsPanel.add(b);
        }
        for (int i = 15; i >= 0; i--) {
            final int bit = i;
            JToggleButton b = new JToggleButton(String.valueOf(bit));
            b.addActionListener(e -> CvmDigital.toggleBit(this, RegMem.selMEM, bit));
            memBitButtons[bit] = b;
            bitsPanel.add(b);
        }
        add(bitsPanel, BorderLayout.WEST);

        // === Таблица помощи ===
        helpTable = new JTable(60, 3);
        helpTable.setEnabled(false);
        helpScrollPane = new JScrollPane(helpTable);
        add(helpScrollPane, BorderLayout.EAST);

        // === Поля from/to PC ===
        JPanel pdPanel = new JPanel();
        fromPC = new JTextField("0", 6);
        toPC = new JTextField("65535", 6);
        pdPanel.add(new JLabel("From:")); pdPanel.add(fromPC);
        pdPanel.add(new JLabel("To:")); pdPanel.add(toPC);
        add(pdPanel, BorderLayout.SOUTH);

        pack();
        setSize(1200, 700);
        setLocationRelativeTo(null);
    }

    // ======================= Методы отображения =======================

    public void displayPC(int value) { dispPC.setText(String.format("%05d", value)); }
    public void displaySP(int value) { dispSP.setText(String.format("%05d", value)); }
    public void displayA(int value)  { dispA.setText(String.format("%05d", value )); }
    public void displayX(int value)  { dispX.setText(String.format("%05d", value )); }
    public void displayRH(int value) { dispRH.setText(String.format("%05d", value & 0xFFFF)); }
    public void displayRL(int value) { dispRL.setText(String.format("%05d", value & 0xFFFF)); }
    public void displayR(float value) { dispR.setText(String.valueOf(value)); }
    public void displayM(int value)   { dispM.setText(String.format("%05d", value & 0xFFFF)); }
    public void displayMR(float value) { dispMR.setText(String.valueOf(value)); }

    public void displayREGButtons(int value) {
        for (int i = 0; i < 16; i++) {
            regBitButtons[i].setSelected((value & (1 << i)) != 0);
        }
    }

    public void displayMEMButtons(int value) {
        for (int i = 0; i < 16; i++) {
            memBitButtons[i].setSelected((value & (1 << i)) != 0);
        }
    }

    public void switchButton(Switch sw, boolean state) {
        // Необязательно, если используем только selectRegister()
    }

    public void switchOffrunButton() {
        runButton.setEnabled(true);
    }

    // ======================= Ввод-вывод =======================

    public int receiveIFromScreen() {
        numberFormed = false;
        numberString = "";
        oldCaretPosition = screenText.getCaretPosition();
        screenText.requestFocusInWindow();
        while (!numberFormed) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
        try {
            return Integer.parseInt(numberString.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public char receiveCFromScreen() {
        charAcquired = false;
        screenText.requestFocusInWindow();
        while (!charAcquired) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
        return typedChar;
    }

    public float receiveRFromScreen() {
        numberFormed = false;
        numberString = "";
        oldCaretPosition = screenText.getCaretPosition();
        screenText.requestFocusInWindow();
        while (!numberFormed) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
        try {
            return Float.parseFloat(numberString.trim());
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public void sendIToScreen(int value) {
        screenText.append(String.valueOf(value) + "\n");
    }

    public void sendCToScreen(char value) {
        screenText.append(String.valueOf(value));
    }

    public void sendRToScreen(float value) {
        screenText.append(String.valueOf(value) + "\n");
    }

    // ======================= Вспомогательные методы =======================

    public static int getBeginPC() { return fromPCValue; }
    public static int getEndPC()   { return toPCValue; }

    public void setEndPC(int endPCValue) {
        toPCValue = endPCValue;
        if (toPC != null) toPC.setText(String.valueOf(endPCValue));
    }

    private void ProgramDeviceInit() {
        fromPC.setText("0");
        toPC.setText("65535");
        fromPCValue = 0;
        toPCValue = 65535;
    }

    private void initScreen() {
        screenText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == '\n' || c == '\r') {
                    numberString = screenText.getText().substring(oldCaretPosition).trim();
                    numberFormed = true;
                    charAcquired = true;
                    typedChar = '\n';
                } else if (!charAcquired) {
                    typedChar = c;
                    charAcquired = true;
                }
            }
        });
    }

    private void initHelp() {
        new HelpAssembler().getHelpText(this);
    }

    public void setCode(int row, int code)   { helpTable.setValueAt(code, row, 0); }
    public void setInstr(int row, String s)  { helpTable.setValueAt(s, row, 1); }
    public void setAttr(int row, String s)   { helpTable.setValueAt(s, row, 2); }

    // ======================= main =======================
//    public static void main(String[] args) {
//
//    }
}