package org.example.stortiessearch.infrastructure.mq.kafka.system.deadletter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaTopicProperties.DEAD_LETTER_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeadLetterProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonSerializer;

    public void publishDeadLetter(DeadLetterEvent event) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .topic(DEAD_LETTER_TOPIC)
            .eventClass(KafkaEvent.class)
            .retryCount(0)
            .payload(jsonSerializer.toJson(event))
            .build();

        kafkaTemplate.send(DEAD_LETTER_TOPIC, kafkaEvent);
    }
}
