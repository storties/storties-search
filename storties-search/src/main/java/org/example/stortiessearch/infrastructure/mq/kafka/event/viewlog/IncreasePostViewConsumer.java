package org.example.stortiessearch.infrastructure.mq.kafka.event.viewlog;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostViewEvent;
import org.example.stortiessearch.domain.post.CommandPostRepository;
import org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.system.retry.KafkaRetryProducer;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

// todo luaScript로 데이터 정합성 보장
@Component
@RequiredArgsConstructor // todo 연결 실패 시 취할 액션 설정
public class IncreasePostViewConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    private final CommandPostRepository commandPostRepository;

    private final JsonSerializer jsonSerializer;

    private final KafkaRetryProducer kafkaRetryProducer;

    private static final Duration TTL = Duration.ofHours(5);

    @KafkaListener(
        topics = KafkaProperties.INCREASE_VIEW_TOPIC,
        groupId = KafkaProperties.GROUP_ID,
        containerFactory = KafkaProperties.CONTAINER_FACTORY
    )
    public void consume(KafkaEvent kafkaEvent, Acknowledgment ack) {
        try {
            String payLoad = kafkaEvent.getPayload();
            IncreasePostViewEvent event = jsonSerializer.fromJson(payLoad, IncreasePostViewEvent.class);

            String deduplicationKey = "viewed:" + event.getUserId() + ":" + event.getPostId();
            String viewCountKey = "post:" + event.getPostId() + ":views";

            Boolean isFirstInRedis = redisTemplate.opsForValue()
                .setIfAbsent(deduplicationKey, "1", TTL);

            if (Boolean.TRUE.equals(isFirstInRedis)) {
                redisTemplate.opsForValue().increment(viewCountKey);
                commandPostRepository.savePostViewLog(event.getPostId(), event.getUserId());
            }
            ack.acknowledge();
        } catch (RuntimeException e) {
            kafkaEvent.setErrorMessage(e.getMessage());
            kafkaRetryProducer.retryPublish(kafkaEvent);

            ack.acknowledge();
        }

    }
}
