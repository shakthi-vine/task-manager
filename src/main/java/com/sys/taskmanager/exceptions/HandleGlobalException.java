package com.sys.taskmanager.exceptions;

import static com.sys.taskmanager.helper.ResponseBuilder.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class HandleGlobalException extends Exception {
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(String message) {
        return new ApiResponse.Builder().success(false).message(message).build();
    }
}
