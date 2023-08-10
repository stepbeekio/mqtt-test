package com.mqtttest.demo;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.mqtttest.demo.broker.Broker;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a very rough and very perilous connection pool.
 */
@Component
public class HiveMqConnectionPool {
    private final Map<Long, Mqtt5BlockingClient> clients = new ConcurrentHashMap<>();
    private final HiveMqConnectionBuilder builder;

    public HiveMqConnectionPool(HiveMqConnectionBuilder builder) {
        this.builder = builder;
    }

    public Mqtt5BlockingClient getClient(Broker broker) {
        if (clients.containsKey(broker.getBrokerId())) {
            return clients.get(broker.getBrokerId());
        } else {
            return createAndPersist(broker);
        }
    }

    public void update(Broker broker) {
        // We don't disconnect old clients - we're just going to leak them.
        // For disconnection to work, we'd need to be able to track which ones are in use which is out of scope.
        createAndPersist(broker);
    }

    private Mqtt5BlockingClient createAndPersist(Broker broker) {
        synchronized (clients) {
            var client = builder.buildAndConnect(broker);
            clients.put(broker.getBrokerId(), client);

            return client;
        }
    }
}
