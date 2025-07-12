package org.example.stortiessearch.infrastructure.mq.kafka.system.deadletter;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DeadLetterEvent {

    private String OriginalTopic;

    private String originalPayload;

    private LocalDateTime failedAt;

    private String lastErrorMessage;
}
