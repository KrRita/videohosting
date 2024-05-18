package com.example.videohosting.exception;

import org.hibernate.sql.Delete;

public class DeleteFileException extends RuntimeException {
    public DeleteFileException(String message) {
        super(message);
    }
}
