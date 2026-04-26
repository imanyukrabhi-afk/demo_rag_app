package com.rag.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model.embedding}")
    private String model;

    public EmbeddingService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(apiUrl).build();
    }

    public List<Double> getEmbedding(String text) {

        Map<String, Object> request = Map.of(
                "input", text,
                "model", model
        );

        Map response = webClient.post()
                .uri("/embeddings")
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (List<Double>) ((Map)((List)response.get("data")).get(0)).get("embedding");
    }
}
