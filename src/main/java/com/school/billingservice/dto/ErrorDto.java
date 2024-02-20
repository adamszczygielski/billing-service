package com.school.billingservice.dto;

import org.springframework.http.HttpStatus;

public record ErrorDto(HttpStatus status, String message) {
}
