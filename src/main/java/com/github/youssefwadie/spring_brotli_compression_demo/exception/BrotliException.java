package com.github.youssefwadie.spring_brotli_compression_demo.exception;

public class BrotliException extends RuntimeException {


    public BrotliException() { }

    public BrotliException(String message) {
        super(message);
    }

    public BrotliException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrotliException(Throwable cause) {
        super(cause);
    }
}
