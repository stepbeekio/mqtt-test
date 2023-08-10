package com.mqtttest.demo.broker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class BrokerController {

    private final BrokerService brokerService;

    public BrokerController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PutMapping("/mqtt/{brokerName}")
    public ResponseEntity<Void> createMessage(@PathVariable String brokerName,
                                              @RequestBody BrokerConnectionDetails details
    ) {
        brokerService.persist(brokerName, details);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/mqtt/{brokerName}")
    public Broker getBrokerDetails(@PathVariable String brokerName) {
        var broker = brokerService.getBroker(brokerName);

        if (broker == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Broker not found with name " + brokerName
            );
        } else {
            return broker;
        }
    }

    @DeleteMapping("/mqtt/{brokerName}")
    public ResponseEntity<Void> deleteBroker(@PathVariable String brokerName) {
        brokerService.delete(brokerName);

        return ResponseEntity.ok().build();
    }
}
