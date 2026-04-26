package com.rag.controller;

import com.rag.model.QueryRequest;
import com.rag.service.QueryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling user queries
 * in RAG-based Document Q&A system.
 */
@RestController
@RequestMapping("/api")
public class QueryController {

    private final QueryService queryService;

    // Constructor Injection (Recommended over field injection)
    @Autowired
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * Endpoint to process user query
     * @param request contains user question
     * @return generated response from RAG pipeline
     */
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody QueryRequest request) {

        if (request == null || request.getQuestion() == null || request.getQuestion().isBlank()) {
            return ResponseEntity.badRequest().body("Question must not be empty");
        }

        String response = queryService.processQuery(request.getQuestion());

        return ResponseEntity.ok(response);
    }
}
