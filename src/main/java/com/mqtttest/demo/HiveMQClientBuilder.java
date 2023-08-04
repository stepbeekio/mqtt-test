package com.mqtttest.demo;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class HiveMQClientBuilder {

    private static Mqtt5BlockingClient client;
    private final String host;
    private final String username;
    private final String password;
    private final int port;

    public HiveMQClientBuilder(
            @Value("${hivemq.host}") String host,
            @Value("${hivemq.username}") String username,
            @Value("${hivemq.password}") String password,
            @Value("${hivemq.port}") int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public synchronized Mqtt5BlockingClient buildAndConnect() throws InterruptedException {
        if (client != null) {
            return client;
        }

        client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(port)
                .sslWithDefaultConfig()
                .buildBlocking();

        // connect to HiveMQ Cloud with TLS and username/pw
        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        return client;
    }
}
