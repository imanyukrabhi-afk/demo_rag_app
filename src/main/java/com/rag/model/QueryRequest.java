package com.rag.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * Request model for query API (/ask)
 */
public class QueryRequest {

    @NotBlank(message = "Question must not be empty")
    
    @Size(
        min = 5,
        max = 500,
        message = "Question must be between 5 and 500 characters"
    )

    @Pattern(
        regexp = "^[a-zA-Z0-9 ?.,!'-]+$",
        message = "Question contains invalid characters"
    )

    private String question;

    public QueryRequest() {}

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
