package com.mqtttest.demo;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;


class DemoApplicationTests extends BaseIntegrationTest {

//    private final String topic = "my/test/topic";
//
//    @Autowired
//    Mqtt5BlockingClient client;
//
//    @Test
//    void basicHiveTest() throws InterruptedException {
//        client.subscribeWith()
//                .topicFilter(topic)
//                .send();
//
//        client.publishWith().topic(topic).payload("Hello World!".getBytes(UTF_8)).send();
//
//        try (Mqtt5BlockingClient.Mqtt5Publishes publishes = client.publishes(MqttGlobalPublishFilter.ALL)) {
//            var result = publishes.receive().getPayloadAsBytes();
//
//            assertThat(new String(result, UTF_8)).isEqualTo("Hello World!");
//        }
//    }

}
