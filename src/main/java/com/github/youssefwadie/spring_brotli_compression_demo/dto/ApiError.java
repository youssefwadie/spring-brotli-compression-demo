package com.github.youssefwadie.spring_brotli_compression_demo.dto;

public record ApiError(int code, String message) {
    public static ApiError badRequest(String message) {
        return new ApiError(400, message);
    }
}
