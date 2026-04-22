package com.example.ts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TsApplication {
  public static void main(String[] args) {
    SpringApplication.run(TsApplication.class, args);
  }

}
