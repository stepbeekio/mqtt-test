package com.mqtttest.demo;

import org.slf4j.event.Level;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.hivemq.HiveMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = {DemoApplication.class})
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    @Container
    static HiveMQContainer hivemqCe = new HiveMQContainer(DockerImageName.parse("hivemq/hivemq-ce").withTag("2021.3"))
            .withLogLevel(Level.DEBUG);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("hivemq.host", hivemqCe::getHost);
        registry.add("hivemq.username", () -> "");
        registry.add("hivemq.password", () -> "");
        registry.add("hivemq.port", hivemqCe::getMqttPort);
    }

}
