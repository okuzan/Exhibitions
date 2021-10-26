package com.example.exhibitions.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class CustomControllerAdvice {

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<?> handle404(CustomErrorException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getStatus(), e.getMessage(), "", e.getData()), e.getStatus());
    }
}