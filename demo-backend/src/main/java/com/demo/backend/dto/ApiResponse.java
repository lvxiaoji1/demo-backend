package com.demo.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;

    private ApiResponse(int code, String message, T data, List<String> errors) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data, null);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null, null);
    }

    public static <T> ApiResponse<T> error(int code, String message, List<String> errors) {
        return new ApiResponse<>(code, message, null, errors);
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public List<String> getErrors() { return errors; }
    public LocalDateTime getTimestamp() { return timestamp; }
}