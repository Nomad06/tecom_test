package com.bubalex.test.exception.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The base class of the REST error.
 */
@Getter
public class RestErrorDetails extends RestError {

    private final Map<String, List<String>> errors;

    /**
     * C-tor.
     */
    @Builder(builderMethodName = "bindingBuilder", builderClassName = "BindingBuilder")
    public RestErrorDetails(HttpStatus status, UUID code, String message, BindingResult bindingResult) {
        super(status, code, message);
        this.errors = bindingResult.getFieldErrors().stream()
                .collect(
                        Collectors.groupingBy(
                                FieldError::getField,
                                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                        )
                );
    }

    /**
     * C-tor.
     */
    @Builder(builderMethodName = "constraintBuilder", builderClassName = "ConstraintBuilder")
    public RestErrorDetails(HttpStatus status, UUID code, String message,
                            ConstraintViolationException constraintViolationException) {
        super(status, code, message);
        this.errors = constraintViolationException.getConstraintViolations().stream()
                .collect(
                        Collectors.groupingBy(
                                c -> c.getPropertyPath().toString(),
                                Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                        )
                );
    }
}
