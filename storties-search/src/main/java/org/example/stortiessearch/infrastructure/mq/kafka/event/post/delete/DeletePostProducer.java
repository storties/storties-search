package org.example.stortiessearch.infrastructure.mq.kafka.event.post.delete;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DeletePostEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.DELETE_TOPIC;

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
