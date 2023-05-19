package com.atrilos.socksrest.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSuchOperationException extends RuntimeException {
    public NoSuchOperationException(String message) {
        super(message);
        log.error(message);
    }
}
