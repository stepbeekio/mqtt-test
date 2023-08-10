package com.mqtttest.demo.broker.topic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class BrokerTopicController {
    private final BrokerTopicServiceImpl brokerTopicService;

    public BrokerTopicController(BrokerTopicServiceImpl brokerTopicService) {
        this.brokerTopicService = brokerTopicService;
    }

    @PostMapping("/mqtt/{brokerName}/send/{topicName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(@PathVariable String brokerName, @PathVariable String topicName, @RequestBody Message message) {
        brokerTopicService.sendMessage(brokerName, topicName, message);
    }


    @GetMapping("/mqtt/{brokerName}/receive/{topicName}")
    public SseEmitter receiveMessages(@PathVariable String brokerName,
                                      @PathVariable String topicName,
                                      @RequestParam(name = "numberOfMessages", defaultValue = "" + Integer.MAX_VALUE) int numberOfMessages) {
        return brokerTopicService.receiveMessages(brokerName, topicName, numberOfMessages);
    }
}
