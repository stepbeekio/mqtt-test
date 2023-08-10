package com.mqtttest.demo;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.mqtttest.demo.broker.Broker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class HiveMqConnectionBuilderImpl implements HiveMqConnectionBuilder {

    private static final Logger logger = LoggerFactory.getLogger(HiveMqConnectionBuilderImpl.class);

    @Override
    public Mqtt5BlockingClient buildAndConnect(Broker broker) {
        Mqtt5BlockingClient client;

        if (broker.requiresAuthentication()) {
            client = MqttClient.builder()
                    .useMqttVersion5()
                    .serverHost(broker.getHost())
                    .serverPort(broker.getPort())
                    .sslWithDefaultConfig()
                    .buildBlocking();
            client.connectWith()
                    .simpleAuth()
                    .username(broker.getUsername())
                    .password(UTF_8.encode(broker.getPassword()))
                    .applySimpleAuth()
                    .send();

            logger.info("Connected to HiveMQ with TLS and username/pw");

        } else {
            client = MqttClient.builder()
                    .useMqttVersion5()
                    .serverHost(broker.getHost())
                    .serverPort(broker.getPort())
                    .buildBlocking();
            client.connect();
            logger.info("Connected to HiveMQ without username/pw");
        }

        return client;
    }
}
