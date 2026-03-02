package com.social.backend.exceptions;

public class InvalidFollowOperationException extends RuntimeException {
    public InvalidFollowOperationException(String message) {
        super(message);
    }
}