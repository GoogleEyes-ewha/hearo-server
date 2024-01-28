package com.gdsc.hearo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HearoApplication {


    public static void main(String[] args) {
        SpringApplication.run(HearoApplication.class, args);
    }

}
