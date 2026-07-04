package com.digitalmart.application.Exceptions;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path
) {
}
