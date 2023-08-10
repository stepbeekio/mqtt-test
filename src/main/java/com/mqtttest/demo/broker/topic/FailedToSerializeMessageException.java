package com.mqtttest.demo.broker.topic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed to serialize message. Please report this error.")
public class FailedToSerializeMessageException extends RuntimeException {
}
