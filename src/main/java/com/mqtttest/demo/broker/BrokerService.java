package com.mqtttest.demo.broker;

import com.mqtttest.demo.HiveMqConnectionPool;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class BrokerService {
    private final HiveMqConnectionPool builder;
    private final BrokerRepository repository;

    public BrokerService(HiveMqConnectionPool builder, BrokerRepository repository) {
        this.builder = builder;
        this.repository = repository;
    }

    public void persist(String brokerName, BrokerConnectionDetails details) {
        var broker = Optional.ofNullable(repository.findByName(brokerName)).map(b -> {
            b.updateConnectionDetails(details);
            return b;
        }).orElseGet(() -> new Broker(brokerName, details));

        repository.save(broker);

        builder.update(broker);
    }

    public Broker getBroker(String brokerName) {
        return repository.findByName(brokerName);
    }

    public void delete(String brokerName) {
        var broker = repository.findByName(brokerName);
        if (broker != null) {
            repository.delete(broker);
        }
    }
}
