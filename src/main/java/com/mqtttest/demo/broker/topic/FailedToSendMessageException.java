package com.mqtttest.demo.broker.topic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Could not send message to broker, please try again")
public class FailedToSendMessageException extends RuntimeException {
    public FailedToSendMessageException(Throwable cause) {
        super(cause);
    }
}
