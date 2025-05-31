package com.hoon.hs.handler;

import com.hoon.hs.exception.ForbiddenException;
import com.hoon.hs.exception.RateLimitException;
import com.hoon.hs.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleResourceNotFoundException(RateLimitException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbiddenException(ForbiddenException ex) {
        return ex.getMessage();
    }
}