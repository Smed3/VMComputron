package com.vmcomputron.model;

import static com.vmcomputron.model.Register.CPU_BIT_VALUES;

public class LoadStoreRequest {
    private String selectedRegister;  // "PC", "SP", "A", "X", "RH", "RL"


    public LoadStoreRequest() {}

    public LoadStoreRequest(String selectedRegister) {
        this.selectedRegister = selectedRegister.toUpperCase();
    }

    public static boolean isValue(int addr) {
        for (int i = 0; i < 16; i++) {
            if (addr >= CPU_BIT_VALUES[i]) {
                addr -= CPU_BIT_VALUES[i];
            }
        }

        if(addr == 0)
            return true;
        return false;
    }


    public String getSelectedRegister() {
        return selectedRegister;
    }

    public void setSelectedRegister(String selectedRegister) {
        this.selectedRegister = selectedRegister.toUpperCase();
    }

}