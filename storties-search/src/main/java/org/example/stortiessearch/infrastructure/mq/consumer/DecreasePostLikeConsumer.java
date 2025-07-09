package org.example.stortiessearch.infrastructure.mq.consumer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DecreasePostLikeEvent;
import org.example.stortiessearch.data.persistence.post.CommandPostRepository;
import org.example.stortiessearch.data.persistence.post.QueryPostRepository;
import org.example.stortiessearch.data.persistence.post.model.PostLikeEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.DECREASE_LIKE_TOPIC;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.GROUP_ID;

@Component
@RequiredArgsConstructor
public class DecreasePostLikeConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    private final CommandPostRepository commandPostRepository;

    private final QueryPostRepository queryPostRepository;

    @KafkaListener(
        topics = DECREASE_LIKE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(DecreasePostLikeEvent event, Acknowledgment ack) {
        String likeCountKey = "post:" + event.getPostId() + ":likes";
        PostLikeEntity postLike = queryPostRepository.queryLikeByPostIdAndUserId(event.getPostId(), event.getUserId());
        if (postLike == null) {
            ack.acknowledge();
            return;
        }

        redisTemplate.opsForValue().decrement(likeCountKey);
        commandPostRepository.deleteLikeById(postLike.getId());

        ack.acknowledge();
    }
}
