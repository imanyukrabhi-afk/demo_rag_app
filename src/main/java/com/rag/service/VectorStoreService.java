package com.rag.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class VectorStoreService {

    private final WebClient webClient;

    @Value("${qdrant.url}")
    private String qdrantUrl;

    @Value("${qdrant.collection}")
    private String collection;

    @Value("${qdrant.topK}")
    private int topK;

    public VectorStoreService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    /**
     * Store embedding in Qdrant
     */
    public void store(List<Double> embedding, String text) {

        Map<String, Object> point = Map.of(
                "id", UUID.randomUUID().toString(),
                "vector", embedding,
                "payload", Map.of("text", text)
        );

        Map<String, Object> request = Map.of(
                "points", List.of(point)
        );

        webClient.put()
                .uri(qdrantUrl + "/collections/" + collection + "/points")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
public void store(List<Double> embedding, String text, Map<String, Object> metadata) {

    Map<String, Object> payload = new HashMap<>(metadata);
    payload.put("text", text);

    Map<String, Object> point = Map.of(
            "id", UUID.randomUUID().toString(),
            "vector", embedding,
            "payload", payload
    );

    Map<String, Object> request = Map.of("points", List.of(point));

    webClient.put()
            .uri(qdrantUrl + "/collections/" + collection + "/points")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(String.class)
            .block();
}
    /**
     * Search similar vectors
     */
    public List<String> search(List<Double> queryVector) {

        Map<String, Object> request = Map.of(
                "vector", queryVector,
                "top", topK
        );

        Map response = webClient.post()
                .uri(qdrantUrl + "/collections/" + collection + "/points/search")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map> results = (List<Map>) response.get("result");

        List<String> texts = new ArrayList<>();
        for (Map r : results) {
            Map payload = (Map) r.get("payload");
            texts.add((String) payload.get("text"));
        }

        return texts;
    }
}
