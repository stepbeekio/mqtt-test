package com.mqtttest.demo.broker;

import com.mqtttest.demo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BrokerControllerTest extends BaseIntegrationTest {

    @Test
    void persistABroker() throws Exception {
        mvc.perform(put("/mqtt/my-test-broker")
                        .content("{\"host\":\"" + hivemqCe.getHost() + "\",\"port\":" + hivemqCe.getMqttPort() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/mqtt/my-test-broker"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"host\":\"" + hivemqCe.getHost() + "\",\"port\":" + hivemqCe.getMqttPort() + "}"));
    }

    @Test
    void updateABroker() throws Exception {
        mvc.perform(put("/mqtt/my-test-broker")
                        .content("{\"host\":\"" + hivemqCe.getHost() + "\",\"port\":" + hivemqCe.getMqttPort() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/mqtt/my-test-broker"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"host\":\"" + hivemqCe.getHost() + "\",\"port\":" + hivemqCe.getMqttPort() + "}"));

        mvc.perform(put("/mqtt/my-test-broker")
                        .content("{\"host\":\"" + alternateHiveMqCe.getHost() + "\",\"port\":" + alternateHiveMqCe.getMqttPort() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/mqtt/my-test-broker"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"host\":\"" + alternateHiveMqCe.getHost() + "\",\"port\":" + alternateHiveMqCe.getMqttPort() + "}"));
    }

    @Test
    void deleteABroker() throws Exception {
        mvc.perform(put("/mqtt/my-test-broker")
                        .content("{\"host\":\"" + hivemqCe.getHost() + "\",\"port\":" + hivemqCe.getMqttPort() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(delete("/mqtt/my-test-broker"))
                .andExpect(status().isOk());

        mvc.perform(get("/mqtt/my-test-broker"))
                .andExpect(status().isNotFound());
    }
}
