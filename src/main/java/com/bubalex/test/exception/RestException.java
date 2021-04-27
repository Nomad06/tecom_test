package com.bubalex.test.exception;

import org.apache.logging.log4j.message.FormattedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * The abstract class of REST exceptions.
 */
public abstract class RestException extends RuntimeException {

    /**
     * C-tor.
     *
     * @param message Message
     */
    public RestException(String message) {
        super(message);
    }

    /**
     * C-tor.
     *
     * @param messagePattern Message pattern with the args placeholders.
     * @param args           Args.
     */
    public RestException(String messagePattern, Object... args) {
        super(new FormattedMessage(messagePattern, args).getFormattedMessage());
    }

    @NonNull
    public abstract HttpStatus getHttpStatus();
}
