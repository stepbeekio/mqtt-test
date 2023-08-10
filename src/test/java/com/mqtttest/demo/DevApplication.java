package com.mqtttest.demo;

import org.springframework.boot.SpringApplication;

public class DevApplication {

    public static void main(String[] args) {
        SpringApplication.from(DemoApplication::main)
                .with(DevConfiguration.class)
                .run(args);
    }
}
