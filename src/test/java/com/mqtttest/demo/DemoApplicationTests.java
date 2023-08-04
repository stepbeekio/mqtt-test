package com.mqtttest.demo;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.charset.StandardCharsets.UTF_8;


class DemoApplicationTests extends BaseIntegrationTest {

    private final String topic = "my/test/topic";

    @Autowired
    HiveMQClientBuilder builder;

    @Test
    void basicHiveTest() throws InterruptedException {
        var client = builder.buildAndConnect();
        var latch = new CountDownLatch(1);
        AtomicReference<String> result = null;

        client.subscribeWith()
                .topicFilter(topic)
                .send();

        client.toAsync().publishes(MqttGlobalPublishFilter.ALL, publish -> {
            result.set(new String(publish.getPayload().get().array(), UTF_8));
            latch.countDown();
        });

        client.publishWith().topic(topic).payload("Hello World!".getBytes(UTF_8)).send();

        latch.await();

    }

}
