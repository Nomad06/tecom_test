package com.bubalex.test.exception.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * The model of REST error.
 */
@Getter
@Builder
@JsonInclude(NON_NULL)
public class RestError {

    private final HttpStatus status;
    private final UUID code;
    private final String message;

    @JsonGetter(value = "status")
    public String getStatusReason() {
        return status.getReasonPhrase();
    }
}
