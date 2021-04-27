package com.bubalex.test.exception;

import org.springframework.http.HttpStatus;

/**
 * A runtime exception indicating a resource requested by a client was not found on the server.
 */
public class NotFoundException extends RestException {

    /**
     * C-tor.
     *
     * @param message Message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * C-tor.
     *
     * @param messagePattern Message pattern with the args placeholders.
     * @param args           Args.
     */
    public NotFoundException(String messagePattern, Object... args) {
        super(messagePattern, args);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
