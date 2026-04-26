package com.rag.controller;

import com.rag.model.IngestRequest;
import com.rag.model.ApiResponse;
import com.rag.service.IngestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling document ingestion
 * in RAG-based system.
 */
@RestController
@RequestMapping("/api")
public class IngestionController {

    private final IngestionService ingestionService;

    @Autowired
    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    /**
     * Endpoint to ingest document
     * @param request contains document content
     * @return success message
     */
    @PostMapping("/ingest")
    public ResponseEntity<String> ingestDocument(@RequestBody IngestRequest request) {

        if (request == null || request.getDocument() == null || request.getDocument().isBlank()) {
            return ResponseEntity.badRequest().body("Document must not be empty");
        }

        ingestionService.ingest(request.getDocument());

        return ResponseEntity.ok(new ApiResponse("Document ingested successfully"));
    }
}
