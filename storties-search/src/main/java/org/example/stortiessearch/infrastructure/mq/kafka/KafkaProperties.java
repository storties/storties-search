package org.example.stortiessearch.infrastructure.mq.kafka;

import java.util.List;

public class KafkaProperties {
    public static final String DELETE_TOPIC = "delete-post";

    public static final String UPDATE_TOPIC = "update-post";

    public static final String CREATE_TOPIC = "create-post";

    public static final String INCREASE_VIEW_TOPIC = "increase-view";

    public static final String INCREASE_LIKE_TOPIC = "increase-like";

    public static final String DECREASE_LIKE_TOPIC = "decrease-like";

    // 재시도 필요한 토픽 등록 필요
    public static final List<String> RETRY_TARGET_TOPICS = List.of(
        DELETE_TOPIC,
        UPDATE_TOPIC,
        CREATE_TOPIC,
        INCREASE_VIEW_TOPIC,
        INCREASE_LIKE_TOPIC,
        DECREASE_LIKE_TOPIC
    );

    public static final String DEAD_LETTER_TOPIC = "post-dlq";

    public static final String RETRY_TOPIC = "retry";

    public static final String GROUP_ID = "post-group";

    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactoryWithManualAck";

    public final static String INDEX_NAME = "storties_post_index";

    public KafkaProperties() {
    }
}
