package com.test.oddschecker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OddsStorageBadRequestException extends RuntimeException {
    public OddsStorageBadRequestException(final String message) {
        super(message);
    }
}
