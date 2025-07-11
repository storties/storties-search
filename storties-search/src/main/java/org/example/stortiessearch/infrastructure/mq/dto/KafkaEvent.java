package org.example.stortiessearch.infrastructure.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class KafkaEvent {

    private String topic;

    private Class<?> eventClass;

    private String payload;

    private int retryCount;

    public void increaseRetryCount() {
        ++ this.retryCount;
    }

    public KafkaEvent() {
    }
}
