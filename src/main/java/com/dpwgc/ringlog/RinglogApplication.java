package com.dpwgc.ringlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class RinglogApplication {

    public static void main(String[] args) {
        SpringApplication.run(RinglogApplication.class, args);
    }

}
