package com.demo.brokagefirmbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BrokageFirmBackendApplication {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("admin"));

        SpringApplication.run(BrokageFirmBackendApplication.class, args);
    }

}
