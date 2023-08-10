package com.mqtttest.demo.broker;

import com.mqtttest.demo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class BrokerServiceTest extends BaseIntegrationTest {

    @Autowired
    private BrokerService brokerService;

    @Test
    void testPersistingANewBrokerWithValidConnectionDetailsWorks() {
        brokerService.persist("test", new BrokerConnectionDetails(hivemqCe.getHost(), hivemqCe.getMqttPort()));
        Broker saved = brokerService.getBroker("test");

        assertThat(saved).isNotNull();
        assertThat(saved.getHost()).isEqualTo(hivemqCe.getHost());
        assertThat(saved.getPort()).isEqualTo(hivemqCe.getMqttPort());
    }

    @Test
    void testDeletingABrokerWorks() {
        brokerService.persist("test", new BrokerConnectionDetails(hivemqCe.getHost(), hivemqCe.getMqttPort()));
        brokerService.delete("test");

        assertThat(brokerService.getBroker("test")).isNull();
    }
}
