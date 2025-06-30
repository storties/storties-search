package org.example.stortiessearch.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.global.exception.error.ErrorCodes;

@Getter
@RequiredArgsConstructor
public class StortiesException extends RuntimeException{

    private final ErrorCodes errorProperty;
}
