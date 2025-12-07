package com.vmcomputron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VMComputronApplication {
    public static void main(String[] args) {
        System.out.println("=== DEBUG INFO ===");
        System.out.println("Working Directory: " + new java.io.File(".").getAbsolutePath());
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        System.out.println("==================");

        SpringApplication.run(VMComputronApplication.class, args);
    }
}