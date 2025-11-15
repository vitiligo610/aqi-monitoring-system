package com.aqi.service;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttSubscriptionService {
    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriptionService.class);
    private final Mqtt3AsyncClient mqttClient;

    @PostConstruct
    public void startMqttClient() {
        mqttClient.connect()
            .whenComplete((connAck, throwable) -> {
                if (throwable != null) {
                    logger.error("Failed to connect to MQTT Broker: {}", throwable.getMessage());
                } else {
                    logger.info("Successfully connected to MQTT Broker.");
                    subscribeToTopics();
                }
            });
    }

    private void subscribeToTopics() {
        final String topicFilter = "sensors/+/data";

        mqttClient.subscribeWith()
            .topicFilter(topicFilter)
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback(publish -> {
                String topic = publish.getTopic().toString();
                String payload = new String(publish.getPayloadAsBytes(), java.nio.charset.StandardCharsets.UTF_8);

                logger.info("Message received on [{}]: {}", topic, payload);
            })
            .send()
            .whenComplete((subAck, throwable) -> {
                if (throwable != null) {
                    logger.error("Failed to subscribe to topic " + topicFilter + ": {}", throwable.getMessage());
                } else {
                    logger.info("Successfully subscribed to topic: {}", topicFilter);
                }
            });
    }
}
