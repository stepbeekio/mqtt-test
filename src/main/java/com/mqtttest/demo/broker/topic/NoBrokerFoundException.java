package com.mqtttest.demo.broker.topic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No broker found with the given name")
public class NoBrokerFoundException extends RuntimeException {
}
