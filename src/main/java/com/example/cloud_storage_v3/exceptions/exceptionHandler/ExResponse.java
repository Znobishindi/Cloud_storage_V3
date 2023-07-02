package com.example.cloud_storage_v3.exceptions.exceptionHandler;

public class ExResponse {
    private String message;
    private long id;

    public ExResponse() {
    }

    public ExResponse(String message,long id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
