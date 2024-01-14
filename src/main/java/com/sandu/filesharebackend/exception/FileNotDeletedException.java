
package com.sandu.filesharebackend.exception;

public class FileNotDeletedException extends RuntimeException {
    public FileNotDeletedException(String message) {
        super(message);
    }
}
