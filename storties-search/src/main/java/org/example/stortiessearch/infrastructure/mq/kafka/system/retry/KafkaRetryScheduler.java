package org.example.stortiessearch.infrastructure.mq.kafka.system.retry;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaRetryScheduler {

    private final RedisTemplate<String, String> redisTemplate;

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonConverter;

    @Scheduled(fixedDelay = 30000)
    public void processShortQueue() {
        for (String topic : KafkaProperties.RETRY_TARGET_TOPICS) {
            process("redis:short:" + topic);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void processLongQueue() {
        for (String topic : KafkaProperties.RETRY_TARGET_TOPICS) {
            process("redis:long:" + topic);
        }
    }

    private void process(String key) {
        long now = System.currentTimeMillis();
        Set<String> messages = redisTemplate.opsForZSet().rangeByScore(key, 0, now);

        for (String kafkaEventJson : messages) {
            redisTemplate.opsForZSet().remove(key, kafkaEventJson);

            KafkaEvent kafkaEvent =
                jsonConverter.fromJson(kafkaEventJson, KafkaEvent.class);

            kafkaTemplate.send(kafkaEvent.getTopic(), kafkaEvent);
        }
    }
}

