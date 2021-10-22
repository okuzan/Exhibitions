package com.example.exhibitions.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
class CustomControllerAdvice {

//    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomErrorException.class)
    public String handle404(CustomErrorException e) {
        System.out.println(e.getMessage());
        return "error/404";
    }
//    @ExceptionHandler(NullPointerException.class) // exception handled
//    public ResponseEntity<ErrorResponse> handleNullPointerExceptions(Exception e) {
//        // ... potential custom logic
//
//        HttpStatus status = HttpStatus.NOT_FOUND; // 404
//        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
//    }
//
//    // fallback method
//    @ExceptionHandler(Exception.class) // exception handled
//    public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {
//        // ... potential custom logic
//
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
//
//        // converting the stack trace to String
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        e.printStackTrace(printWriter);
//        String stackTrace = stringWriter.toString();
//
//        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace // specifying the stack trace in case of 500s
//        ), status);
//    }
//
//    @ExceptionHandler(ItemNotFound.class)
//    public ResponseEntity<ErrorResponse> handleCustomDataNotFoundExceptions(Exception e) {
//        HttpStatus status = HttpStatus.NOT_FOUND; // 404
//
//        // converting the stack trace to String
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        e.printStackTrace(printWriter);
//        String stackTrace = stringWriter.toString();
//
//        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace), status);
//    }
//
//    @ExceptionHandler(CustomErrorException.class)
//    public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(Exception e) {
//        // casting the generic Exception e to CustomErrorException
//        CustomErrorException customErrorException = (CustomErrorException) e;
//
//        HttpStatus status = customErrorException.getStatus();
//
//        // converting the stack trace to String
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        customErrorException.printStackTrace(printWriter);
//        String stackTrace = stringWriter.toString();
//
//        return new ResponseEntity<>(new ErrorResponse(status, customErrorException.getMessage(), stackTrace, customErrorException.getData()), status);
//    }
}