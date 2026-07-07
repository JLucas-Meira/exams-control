package br.com.joaolucas.exams_control.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExists(ResourceAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
