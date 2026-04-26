package com.rag.model;

/**
 * Request model for query API (/ask)
 */
public class QueryRequest {

    private String question;

    public QueryRequest() {
    }

    public QueryRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
