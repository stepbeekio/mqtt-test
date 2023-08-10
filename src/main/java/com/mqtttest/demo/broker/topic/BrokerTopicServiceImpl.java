package com.mqtttest.demo.broker.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.mqtttest.demo.HiveMqConnectionPool;
import com.mqtttest.demo.broker.BrokerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BrokerTopicServiceImpl implements BrokerTopicService {
    private static final Logger logger = LoggerFactory.getLogger(BrokerTopicServiceImpl.class);

    private final BrokerRepository brokerRepository;
    private final HiveMqConnectionPool connectionPool;

    private final ObjectMapper objectMapper;

    private final ExecutorService executorService = Executors.newWorkStealingPool();

    public BrokerTopicServiceImpl(BrokerRepository brokerRepository, HiveMqConnectionPool connectionPool, ObjectMapper objectMapper) {
        this.brokerRepository = brokerRepository;
        this.connectionPool = connectionPool;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendMessage(String brokerName, String topicName, Message message) {
        var broker = brokerRepository.findByName(brokerName);
        if (broker != null) {
            var connection = connectionPool.getClient(broker);
            byte[] payload = serializeMessage(message);

            var result = connection.publishWith()
                    .topic(topicName)
                    .payload(payload)
                    .send();

            if (result.getError().isPresent()) {
                logger.error("Failed to send message to broker " + brokerName + " with topic " + topicName, result.getError().get());
                throw new FailedToSendMessageException(result.getError().get());
            }
        } else {
            throw new NoBrokerFoundException();
        }
    }

    private byte[] serializeMessage(Message message) {
        try {
            return objectMapper.writeValueAsBytes(message);
        } catch (JsonProcessingException e) {
            throw new FailedToSerializeMessageException();
        }
    }

    @Override
    public SseEmitter receiveMessages(String brokerName, String topicName, int numberOfMessages) {
        final var emitter = new SseEmitter();
        final var broker = brokerRepository.findByName(brokerName);
        if (broker != null) {
            final var connection = connectionPool.getClient(broker);
            executorService.submit(() -> {
                        try (final Mqtt5BlockingClient.Mqtt5Publishes publishes = connection.publishes(MqttGlobalPublishFilter.ALL)) {
                            connection.subscribeWith().topicFilter(topicName).send();

                            final var shouldContinue = new AtomicBoolean(true);
                            final var messagesSent = new AtomicInteger(0);

                            emitter.onCompletion(() -> {
                                shouldContinue.set(false);
                            });

                            while (shouldContinue.get() && messagesSent.get() < numberOfMessages) {
                                final Mqtt5Publish publish = publishes.receive();

                                var message = objectMapper.readValue(publish.getPayloadAsBytes(), Message.class);

                                emitter.send(SseEmitter.event().name("message").data(message, MediaType.APPLICATION_JSON));
                                messagesSent.incrementAndGet();
                            }

                            emitter.complete();
                        } catch (InterruptedException | IOException e) {
                            emitter.completeWithError(e);
                            throw new RuntimeException(e);
                        }
                    }
            );

            return emitter;
        } else {
            throw new NoBrokerFoundException();
        }
    }
}
