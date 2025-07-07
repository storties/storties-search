package org.example.stortiessearch.infrastructure.mq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.data.search.document.PostDocument;
import org.example.stortiessearch.data.search.repository.PostSearchRepository;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.infrastructure.client.rest.VectorRestClient;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.example.stortiessearch.infrastructure.search.support.ESPostUpdateUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.GROUP_ID;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.UPDATE_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdatePostConsumer {

    private final ESPostUpdateUseCase esPostUpdateUseCase;

    private final PostSearchRepository postSearchRepository;

    private final VectorRestClient vectorRestClient;

    @KafkaListener(
        topics = UPDATE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(UpdatePostEvent event, Acknowledgment ack) {
        try {
            PostDocument postDocument = postSearchRepository.findById(event.getId())
                .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException);

            String documentId = String.valueOf(postDocument.getId());

            // title or content 변경될 시 벡터 변경
            if(!event.getTitle().equals(postDocument.getTitle()) || !event.getContent().equals(postDocument.getContent())) {
                float[] vector = vectorRestClient.generateVector(documentId, event.getTitle(), event.getContent());
                esPostUpdateUseCase.updateVector(KafkaProperties.INDEX_NAME, documentId, vector);
            }

            esPostUpdateUseCase.updateChangedFields(KafkaProperties.INDEX_NAME, documentId, postDocument, event);

            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
