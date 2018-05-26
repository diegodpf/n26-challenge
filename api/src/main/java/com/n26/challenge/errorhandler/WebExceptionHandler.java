package com.n26.challenge.errorhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> error = new HashMap<>();

        error.put("message", "request has validation errors");

        for (FieldError f : ex.getFieldErrors()) {
            error.put(f.getField(), String.format("%s", f.getDefaultMessage()));
        }


        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> error = new HashMap<>();

        error.put("message", "request has validation errors");

        for (FieldError f : ex.getBindingResult().getFieldErrors()) {
            error.put(f.getField(), String.format("%s", f.getDefaultMessage()));
        }


        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> error = new HashMap<>();

        error.put("message", "request not readable");

        return ResponseEntity.unprocessableEntity().body(error);
    }
}
