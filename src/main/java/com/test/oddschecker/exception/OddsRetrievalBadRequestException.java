package com.test.oddschecker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OddsRetrievalBadRequestException extends RuntimeException {

    public OddsRetrievalBadRequestException(final String message) {
        super(message);
    }
}
