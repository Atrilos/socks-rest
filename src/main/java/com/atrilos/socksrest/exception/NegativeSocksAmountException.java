package com.atrilos.socksrest.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NegativeSocksAmountException extends RuntimeException {
    public NegativeSocksAmountException(String message) {
        super(message);
        log.error(message);
    }
}
