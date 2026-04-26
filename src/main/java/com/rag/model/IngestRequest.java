package com.rag.model;

/**
 * Request model for document ingestion
 */
public class IngestRequest {

    private String document;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
