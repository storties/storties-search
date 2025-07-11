package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DecreasePostLikeEvent;
import org.example.stortiessearch.application.event.DeletePostEvent;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.example.stortiessearch.infrastructure.mq.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.DELETE_TOPIC;

@RequiredArgsConstructor
@Component
public class DeletePostProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonSerializer;

    public void publish(DeletePostEvent event) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .topic(DELETE_TOPIC)
            .eventClass(DeletePostEvent.class)
            .payload(jsonSerializer.toJson(event))
            .retryCount(0)
            .build();

        kafkaTemplate.send(DELETE_TOPIC, kafkaEvent);
    }
}
