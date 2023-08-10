package com.mqtttest.demo;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.mqtttest.demo.broker.Broker;

public interface HiveMqConnectionBuilder {
    Mqtt5BlockingClient buildAndConnect(Broker broker);
}
