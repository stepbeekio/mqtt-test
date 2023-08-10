package com.mqtttest.demo.broker;

public record BrokerConnectionDetails(
        String host,
        int port,
        String username,
        String password
) {

    public BrokerConnectionDetails(String host, int port) {
        this(host, port, null, null);
    }
}
