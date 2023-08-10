package com.mqtttest.demo.broker.topic;

import com.mqtttest.demo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BrokerTopicControllerTest extends BaseIntegrationTest {

    @Autowired
    BrokerTopicServiceImpl brokerTopicService;

    @Test
    void testSubscribeToATopicAndReceiveAFewMessages() throws Exception {
        // Create broker
        mvc.perform(put("/mqtt/my-test-broker")
                        .content("{\"host\":\"" + hivemqCe.getHost() + "\",\"port\":" + hivemqCe.getMqttPort() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        // I've run into issues with mockmvc and sse testing. I've added a hack with the numberOfMessages query parameter that
        // will complete the stream after two messages.
        MvcResult mvcResult = mvc.perform(get("/mqtt/my-test-broker/receive/test-topic?numberOfMessages=2"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        // Send message to topic
        mvc.perform(post("/mqtt/my-test-broker/send/test-topic")
                        .content("{\"message\": \"test-message-1\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // Send message to topic
        mvc.perform(post("/mqtt/my-test-broker/send/test-topic")
                        .content("{\"message\": \"test-message-2\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"message\":\"test-message-1\"")))
                .andExpect(content().string(containsString("\"message\":\"test-message-2\"")));
    }

    @Test
    void testSubscribeToATopicAndReceiveAMessage() throws Exception {
        // Create broker
        mvc.perform(put("/mqtt/my-test-broker")
                        .content("{\"host\":\"" + hivemqCe.getHost() + "\",\"port\":" + hivemqCe.getMqttPort() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        MvcResult mvcResult = mvc.perform(get("/mqtt/my-test-broker/receive/test-topic?numberOfMessages=1"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        // Send message to topic
        mvc.perform(post("/mqtt/my-test-broker/send/test-topic")
                        .content("{\"message\": \"test-message\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"message\":\"test-message\"")));
    }

}
