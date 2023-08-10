# MQTT Demo in Java

Tests rely on testcontainers so you should be able to run easily on Java 20 on a machine with docker installed.

## Running the tests

```bash
gradle build
```


## Heroku Deployment
An instance of the app has been deployed to Heroku: https://mqtt-demo-12d0ff42275a.herokuapp.com/ with an existing broker named `test-broker`.

You can subscribe to messages on the broker with:

```bash
curl https://mqtt-demo-12d0ff42275a.herokuapp.com/mqtt/test-broker/receive/test-topic
```

And then in another tab send messages with:

```bash
curl -X POST https://mqtt-demo-12d0ff42275a.herokuapp.com/mqtt/test-broker/send/test-topic -H 'Content-Type: application/json' -d '{"message": "Hello World"}'
```

Alternatively, you can send a number of messages by running:

```bash
cat vegeta_heroku_example.txt | vegeta attack -rate=10 -duration=30s | tee results.bin | vegeta report 
```

## Running the application locally

The application starts a postgresql database and a HiveMQ test container on each run. You can start in development mode
by running the DevApplication class in test sources.

Some example calls with curl:

```bash
curl -v -X PUT 'http://localhost:8080/mqtt/test-broker' -H 'Content-Type: application/json' -d '{"host": "localhost", "port": "$HIVEMQ_PORT"}'
```

Which will create a broker called `test-broker`.

You can subscribe to messages on the broker with:

```bash
curl http://localhost:8080/mqtt/test-broker/receive/test-topic
```

And then in another tab send messages with:

```bash
curl -X POST http://localhost:8080/mqtt/test-broker/send/test-topic -H 'Content-Type: application/json' -d '{"message": "Hello World"}'
```

Alternatively, you can use vegeta to run send many messages:

```bash
cat vegeta_example.txt | vegeta attack -rate=10 -duration=30s | tee results.bin | vegeta report 
```

And get some statistics about throughput etc.

## Using with Loom

I built this in Java 20 because I thought being able to test the blocking client with and without loom would be
interesting. You can follow this guide to run in IntelliJ with
Loom: https://foojay.io/today/how-to-run-project-loom-from-intellij-idea/ 


Some basic - read: unreliable - perf comparisons of running the vegeta test with 10,000 messages/sec being sent with a 
single subscriber to the SSE feed for 30 seconds on a machine with 64GiB of RAM and a 5950x. I did two runs and threw 
the first out as a warm-up run each time. 

### With Loom
~/H/mqtt-test ❯❯❯ cat vegeta_example.txt | vegeta attack -rate=10000 -duration=30s | tee results.bin | vegeta report
Requests      [total, rate, throughput]         300000, 10000.06, 9999.96
Duration      [total, attack, wait]             30s, 30s, 287.282µs
Latencies     [min, mean, 50, 90, 95, 99, max]  203.623µs, 350.425µs, 318.256µs, 447.656µs, 501.908µs, 661.802µs, 20.482ms
Bytes In      [total, mean]                     0, 0.00
Bytes Out     [total, mean]                     9600000, 32.00
Success       [ratio]                           100.00%
Status Codes  [code:count]                      201:300000  
Error Set:

### Without Loom
~/H/mqtt-test ❯❯❯ cat vegeta_example.txt | vegeta attack -rate=10000 -duration=30s | tee results.bin | vegeta report
Requests      [total, rate, throughput]         300000, 10000.06, 9999.96
Duration      [total, attack, wait]             30s, 30s, 308.732µs
Latencies     [min, mean, 50, 90, 95, 99, max]  204.084µs, 358.776µs, 317.99µs, 433.713µs, 497.003µs, 759.806µs, 20.026ms
Bytes In      [total, mean]                     0, 0.00
Bytes Out     [total, mean]                     9600000, 32.00
Success       [ratio]                           100.00%
Status Codes  [code:count]                      201:300000  
Error Set:

So... virtually indistinguishible. I'd need to spend a fair bit more time digging in to understand whether the issue was the setup or the test run. 

Attempting with a higher number of file descriptors allowed by using `ulimit -u` did not allow much greater throughput. 
My suspicion is that I'd need to toggle some settings in spring boot to enable loom for the web threads.

