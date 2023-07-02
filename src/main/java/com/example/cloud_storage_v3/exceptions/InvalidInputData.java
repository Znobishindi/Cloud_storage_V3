package com.example.cloud_storage_v3.exceptions;

import lombok.Getter;

@Getter
public class InvalidInputData extends RuntimeException{
    private final long id;

    public InvalidInputData(String msg, long id) {
        super(msg);
        this.id = id;
    }
}
