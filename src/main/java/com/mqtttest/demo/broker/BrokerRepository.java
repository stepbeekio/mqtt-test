package com.mqtttest.demo.broker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerRepository extends CrudRepository<Broker, Long> {

    Broker findByName(String brokerName);
}
