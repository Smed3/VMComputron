package com.vmcomputron;

import com.vmcomputron.model.Register;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class VMComputronApplication {

    public static void main(String[] args) {
        Register register = new Register("R", 7);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(Arrays.toString(register.getCpu()));
        SpringApplication.run(VMComputronApplication.class, args);
    }
}