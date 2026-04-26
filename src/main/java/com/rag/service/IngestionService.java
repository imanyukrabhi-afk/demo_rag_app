package com.rag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles document ingestion for RAG pipeline:
 * - Splits document into chunks
 * - Generates embeddings
 * - Stores vectors in Qdrant
 */
@Service
public class IngestionService {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    @Value("${rag.chunk-size}")
    private int chunkSize;

    @Value("${rag.chunk-overlap}")
    private int chunkOverlap;

    @Autowired
    public IngestionService(EmbeddingService embeddingService,
                            VectorStoreService vectorStoreService) {
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
    }

    /**
     * Main ingestion entry point
     */
    public void ingest(String document) {

        validateDocument(document);

        // Step 1: Split document into chunks
        List<String> chunks = splitIntoChunks(document);

        // Step 2: Generate embeddings and store
        for (String chunk : chunks) {

            List<Double> embedding = embeddingService.getEmbedding(chunk);

            // Add metadata (important for real-world filtering)
            Map<String, Object> metadata = Map.of(
                    "source", "uploaded_document",
                    "length", chunk.length(),
                    "timestamp", System.currentTimeMillis()
            );

            vectorStoreService.store(embedding, chunk, metadata);
        }
    }

    /**
     * Splits text into overlapping chunks
     */
    private List<String> splitIntoChunks(String document) {

        List<String> chunks = new ArrayList<>();

        int start = 0;
        int length = document.length();

        while (start < length) {

            int end = Math.min(start + chunkSize, length);

            String chunk = document.substring(start, end).trim();

            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            start += (chunkSize - chunkOverlap);
        }

        return chunks;
    }

    /**
     * Basic validation
     */
    private void validateDocument(String document) {
        if (document == null || document.isBlank()) {
            throw new IllegalArgumentException("Document cannot be null or empty");
        }
    }
}
