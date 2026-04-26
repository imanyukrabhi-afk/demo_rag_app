package com.rag.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryService {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;
    private final LLMService llmService;

    public QueryService(EmbeddingService embeddingService,
                        VectorStoreService vectorStoreService,
                        LLMService llmService) {
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
        this.llmService = llmService;
    }

    public String processQuery(String question) {

        // Step 1: Convert query → embedding
        List<Double> queryVector = embeddingService.getEmbedding(question);

        // Step 2: Retrieve context
        List<String> contextList = vectorStoreService.search(queryVector);

        String context = String.join("\n", contextList);

        // Step 3: Build prompt
        String prompt = "Answer based on context:\n" + context +
                "\n\nQuestion: " + question;

        // Step 4: Call LLM
        return llmService.generateResponse(prompt);
    }
}
