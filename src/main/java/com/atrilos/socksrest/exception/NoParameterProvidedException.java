package com.atrilos.socksrest.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoParameterProvidedException extends RuntimeException {
    public NoParameterProvidedException(String message) {
        super(message);
        log.error(message);
    }
}
