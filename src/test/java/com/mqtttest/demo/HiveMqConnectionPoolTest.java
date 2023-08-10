package com.mqtttest.demo;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.mqtttest.demo.broker.Broker;
import com.mqtttest.demo.broker.BrokerConnectionDetails;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HiveMqConnectionPoolTest {


    @Test
    void testConnectionPoolReturnsCachedClient() {
        var brokerA = new Broker(1L, "brokerA", "localhost", 1883, null, null, Instant.now(), Instant.now());
        var brokerB = new Broker(2L, "brokerB", "localhost", 1884, null, null, Instant.now(), Instant.now());

        var pool = new HiveMqConnectionPool(new StubBuilder());

        var clientA = pool.getClient(brokerA);
        var repeatedClientA = pool.getClient(brokerA);
        var clientB = pool.getClient(brokerB);

        assertThat(clientA).isSameAs(repeatedClientA);
        assertThat(clientA).isNotSameAs(clientB);
    }

    @Test
    void testUpdateWillCreateAndSaveNewClientInCachedPool() {
        var broker = new Broker(1L, "brokerA", "localhost", 1883, null, null, Instant.now(), Instant.now());

        var pool = new HiveMqConnectionPool(new StubBuilder());

        var clientA = pool.getClient(broker);
        broker.updateConnectionDetails(new BrokerConnectionDetails("localhost", 1884));
        pool.update(broker);

        var clientB = pool.getClient(broker);
        assertThat(clientA).isNotSameAs(clientB);
    }

    private static class StubBuilder implements HiveMqConnectionBuilder {
        @Override
        public Mqtt5BlockingClient buildAndConnect(Broker broker) {
            return mock(Mqtt5BlockingClient.class);
        }
    }
}
