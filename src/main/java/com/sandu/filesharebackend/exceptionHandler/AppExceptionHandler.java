package com.sandu.filesharebackend.exceptionHandler;

import com.sandu.filesharebackend.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class AppExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileExpiredException.class)
    public ResponseEntity<String> handleFileExpiredException(FileExpiredException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidFileNameException.class)
    public ResponseEntity<String> handleInvalidFileNameException(InvalidFileNameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPasswordException(WrongPasswordException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(InvalidFileSizeException.class)
    public ResponseEntity<String> handleInvalidFileSizeException(InvalidFileSizeException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileNotSavedException.class)
    public ResponseEntity<String> handleFileNotSavedException(FileNotSavedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileNotDeletedException.class)
    public ResponseEntity<String> handleFileNotDeletedException(FileNotDeletedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }
}
