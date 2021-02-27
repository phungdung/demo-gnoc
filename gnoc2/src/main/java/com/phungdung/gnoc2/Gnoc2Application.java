package com.phungdung.gnoc2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.phungdung.gnoc2.common.dto")
@EntityScan(basePackages = "com.phungdung.gnoc2.common.Model")
@SpringBootApplication
public class Gnoc2Application {

    public static void main(String[] args) {
        SpringApplication.run(Gnoc2Application.class, args);
    }
}
