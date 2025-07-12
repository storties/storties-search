package org.example.stortiessearch.infrastructure.mq.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.infrastructure.mq.dto.KafkaEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.RETRY_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaRetryProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    public void retryPublish(KafkaEvent kafkaEvent) {
        kafkaTemplate.send(RETRY_TOPIC, kafkaEvent);
    }
}
