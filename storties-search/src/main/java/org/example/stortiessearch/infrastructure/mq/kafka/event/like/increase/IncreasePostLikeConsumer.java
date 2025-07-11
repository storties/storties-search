package org.example.stortiessearch.infrastructure.mq.kafka.event.like.increase;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostLikeEvent;
import org.example.stortiessearch.domain.post.CommandPostRepository;
import org.example.stortiessearch.domain.post.QueryPostRepository;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.system.retry.KafkaRetryProducer;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaProperties.GROUP_ID;
import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaTopicProperties.INCREASE_LIKE_TOPIC;

@Component
@RequiredArgsConstructor
public class IncreasePostLikeConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    private final QueryPostRepository queryPostRepository;

    private final CommandPostRepository commandPostRepository;

    private final JsonSerializer jsonSerializer;

    private final KafkaRetryProducer kafkaRetryProducer;

    @KafkaListener(
        topics = INCREASE_LIKE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(KafkaEvent kafkaEvent, Acknowledgment ack) {
        try {
            String payload = kafkaEvent.getPayload();
            IncreasePostLikeEvent event = jsonSerializer.fromJson(payload, IncreasePostLikeEvent.class);

            String likeCountKey = "post:" + event.getPostId() + ":likes";

            boolean isFirst = (queryPostRepository
                .queryLikeByPostIdAndUserId(event.getPostId(), event.getUserId()) == null);

            if (isFirst) {
                commandPostRepository.savePostLike(event.getPostId(), event.getUserId());
                redisTemplate.opsForValue().increment(likeCountKey);
            }

            ack.acknowledge();
        } catch (RuntimeException e) {
            kafkaEvent.setErrorMessage(e.getMessage());
            kafkaRetryProducer.retryPublish(kafkaEvent);

            ack.acknowledge();
        }
    }
}
