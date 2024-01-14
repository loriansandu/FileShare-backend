package com.sandu.filesharebackend.exception;

public class FileNotSavedException extends RuntimeException {
    public FileNotSavedException(String message) {
        super(message);
    }
}
