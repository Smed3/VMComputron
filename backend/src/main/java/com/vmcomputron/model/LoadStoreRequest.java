package com.vmcomputron.model;

public class LoadStoreRequest {
    private String selectedRegister;  // "PC", "SP", "A", "X", "RH", "RL"

    // Конструктор, геттер, сеттер
    public LoadStoreRequest() {}

    public LoadStoreRequest(String selectedRegister) {
        this.selectedRegister = selectedRegister.toUpperCase();
    }

    public String getSelectedRegister() {
        return selectedRegister;
    }

    public void setSelectedRegister(String selectedRegister) {
        this.selectedRegister = selectedRegister.toUpperCase();
    }
}