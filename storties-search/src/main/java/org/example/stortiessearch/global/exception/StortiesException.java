package org.example.stortiessearch.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.global.exception.error.ErrorCodes;

@Getter
public class StortiesException extends RuntimeException{

    private final ErrorCodes errorCodes;

    public StortiesException(ErrorCodes errorCodes) {
        super(errorCodes.getMessage());
        this.errorCodes = errorCodes;
    }
}
