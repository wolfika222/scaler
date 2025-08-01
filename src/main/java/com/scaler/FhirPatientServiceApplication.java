package com.scaler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class FhirPatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FhirPatientServiceApplication.class, args);
    }
}
