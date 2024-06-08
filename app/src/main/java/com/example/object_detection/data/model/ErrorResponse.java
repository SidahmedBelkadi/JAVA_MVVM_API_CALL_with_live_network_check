package com.example.object_detection.data.model;

import java.util.Map;

public class ErrorResponse {
    private String message;
    private Map<String, String[]> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String[]> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String[]> errors) {
        this.errors = errors;
    }
}
