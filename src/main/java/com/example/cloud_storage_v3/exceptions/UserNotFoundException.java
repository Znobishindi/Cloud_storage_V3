package com.example.cloud_storage_v3.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final long id;

    public UserNotFoundException(String msg, long id) {
        super(msg);
        this.id = id;
    }
}