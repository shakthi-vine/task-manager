package com.sys.taskmanager.service;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;
    private final String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
