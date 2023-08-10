package com.mqtttest.demo;

import org.slf4j.event.Level;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.hivemq.HiveMQContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class DevConfiguration {

    @Bean
    HiveMQContainer hivemqCe() {
        return new HiveMQContainer(DockerImageName.parse("hivemq/hivemq-ce").withTag("2021.3"))
                .withLogLevel(Level.DEBUG)
                .withReuse(true);
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresqlContainer() {
        return new PostgreSQLContainer<>("postgres:14.8") // Matches Heroku postgres version
                .withDatabaseName("test")
                .withUsername("sa")
                .withPassword("sa");
    }
}
