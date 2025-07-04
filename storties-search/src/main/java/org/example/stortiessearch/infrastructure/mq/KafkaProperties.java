package org.example.stortiessearch.infrastructure.mq;

public class KafkaProperties {
    public static final String DELETE_TOPIC = "delete-post";

    public static final String UPDATE_TOPIC = "update-post";

    public static final String CREATE_TOPIC = "crate-post";

    public static final String GROUP_ID = "post-group";

    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactoryWithManualAck";

    public final static String INDEX_NAME = "storties_post_index";
}
