package org.example.stortiessearch.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final int status;

    private final String message;

    private final int sequence;

    public static ErrorResponse of(ErrorCodes errorProperty) {
        return new ErrorResponse(errorProperty.getStatus(), errorProperty.getMessage(), errorProperty.getSequence());
    }
}
