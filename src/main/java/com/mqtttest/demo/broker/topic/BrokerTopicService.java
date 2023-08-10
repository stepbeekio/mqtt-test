package com.mqtttest.demo.broker.topic;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface BrokerTopicService {
    void sendMessage(String brokerName, String topicName, Message message);

    SseEmitter receiveMessages(String brokerName, String topicName, int numberOfMessages);
}
