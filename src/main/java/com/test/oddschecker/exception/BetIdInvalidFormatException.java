package com.test.oddschecker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BetIdInvalidFormatException extends RuntimeException {
    public BetIdInvalidFormatException(final String message) {
        super(message);
    }
}
