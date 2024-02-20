package com.school.billingservice.exception;

import com.school.billingservice.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> handleException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleException(DateTimeException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
