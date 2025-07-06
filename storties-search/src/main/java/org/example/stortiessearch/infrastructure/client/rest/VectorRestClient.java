package org.example.stortiessearch.infrastructure.client.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.stortiessearch.infrastructure.client.rest.dto.VectorResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class VectorRestClient {

    private final static String URL = "http://localhost:5000/embed";

    private final ObjectMapper objectMapper;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    public float[] generateVector(String postId, String postTitle, String postContent) {
        try {
            Map<String, String> requestBody = Map.of("sentence", postTitle + postContent);
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpUriRequest request = RequestBuilder.post(URL)
                .setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8)))
                .setHeader("X-Trace-Id", "some-trace-id")
                .setHeader("Accept", "application/json")
                .build();

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());

                VectorResponse vectorResponse = objectMapper.readValue(json, VectorResponse.class);
                float[] vector = vectorResponse.getVector();

                log.info("Vector successfully generated." + postId);

                return vector;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
