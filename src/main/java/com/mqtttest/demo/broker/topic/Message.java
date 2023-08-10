package com.mqtttest.demo.broker.topic;

import java.time.Instant;

public record Message(String message, Instant timestamp) {
    public Message(String message, Instant timestamp) {
        this.message = message;
        this.timestamp = timestamp == null ? Instant.now() : timestamp;
    }
}
