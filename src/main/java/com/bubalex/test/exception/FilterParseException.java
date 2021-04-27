package com.bubalex.test.exception;

import org.springframework.http.HttpStatus;

/**
 * The base exception for all exceptions related to filter parsing.
 */
public abstract class FilterParseException extends RestException {

    /**
     * C-tor.
     *
     * @param message Message
     */
    public FilterParseException(String message) {
        super(message);
    }

    /**
     * C-tor.
     *
     * @param messagePattern Message pattern with the args placeholders.
     * @param args           Args.
     */
    public FilterParseException(String messagePattern, Object... args) {
        super(messagePattern, args);
    }

    /**
     * Http status will be returned.
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
