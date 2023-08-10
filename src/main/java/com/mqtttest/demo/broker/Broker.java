package com.mqtttest.demo.broker;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Objects;

@Table("broker")
public class Broker {
    @Id
    private Long brokerId;
    private String name;
    private String host;
    private int port;
    private String username;
    private String password;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    protected Broker() {}
    public Broker(String name, String host, int port, String username, String password) {
        this.brokerId = null;
        this.name = name;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Broker(String brokerName, BrokerConnectionDetails details) {
        this(brokerName, details.host(), details.port(), details.username(), details.password());
    }

    public Broker(Long brokerId, String name, String host, int port, String username, String password, Instant createdAt, Instant updatedAt) {
        this.brokerId = brokerId;
        this.name = name;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getBrokerId() {
        return brokerId;
    }

    public void updateConnectionDetails(BrokerConnectionDetails details) {
        this.host = details.host();
        this.port = details.port();
        this.username = details.username();
        this.password = details.password();
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean requiresAuthentication() {
        return username != null && password != null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Broker broker = (Broker) o;
        return port == broker.port && Objects.equals(brokerId, broker.brokerId) && Objects.equals(name, broker.name) && Objects.equals(host, broker.host) && Objects.equals(username, broker.username) && Objects.equals(password, broker.password) && Objects.equals(createdAt, broker.createdAt) && Objects.equals(updatedAt, broker.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brokerId, name, host, port, username, password, createdAt, updatedAt);
    }
}
