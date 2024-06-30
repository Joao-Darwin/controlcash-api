package com.controlcash.app.exceptions.handler;

import com.controlcash.app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
@RestController
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseEntityException> handlerAllExceptions(Exception exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntityException);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseEntityException> handlerUserNotFoundException(Exception exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityException);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ResponseEntityException> handlerTransactionNotFoundException(Exception exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityException);
    }

    @ExceptionHandler(GoalNotFoundException.class)
    public ResponseEntity<ResponseEntityException> handlerGoalNotFoundException(Exception exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityException);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ResponseEntityException> handlerCategoryNotFoundException(Exception exception, WebRequest webRequest) {
        ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), exception.getMessage(), webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityException);
    }
}
