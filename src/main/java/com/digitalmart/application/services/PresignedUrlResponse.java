package com.digitalmart.application.services;

public record PresignedUrlResponse(
        String uploadUrl,
        String finalUrl
) {
}
