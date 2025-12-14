package com.vmcomputron;

import com.vmcomputron.model.Register;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class VMComputronApplication {

    public static void main(String[] args) {
        SpringApplication.run(VMComputronApplication.class, args);
    }
}