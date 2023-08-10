CREATE TABLE broker
(
    broker_id  BIGSERIAL PRIMARY KEY,
    name       VARCHAR   NOT NULL UNIQUE,
    host       VARCHAR   NOT NULL,
    port       INT       NOT NULL,
    username   VARCHAR,
    password   VARCHAR,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

