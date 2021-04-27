package com.bubalex.test.exception.handler;

import com.bubalex.test.exception.RestException;
import com.bubalex.test.exception.model.RestError;
import com.bubalex.test.exception.model.RestErrorDetails;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.surefire.shared.lang3.ClassUtils;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The abstract class of the REST exception handler.
 */
@Log4j2
public abstract class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("REST API validation error: {}", ex.getMessage());

        RestErrorDetails errorDetails = RestErrorDetails.bindingBuilder()
                .message("Incorrect request parameters.")
                .status(HttpStatus.BAD_REQUEST)
                .bindingResult(ex.getBindingResult())
                .build();

        return new ResponseEntity<>(errorDetails, errorDetails.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        log.error("REST API bind error: {}", ex.getMessage());

        RestErrorDetails errorDetails = RestErrorDetails.bindingBuilder()
                .message("Incorrect request parameters.")
                .status(HttpStatus.BAD_REQUEST)
                .bindingResult(ex.getBindingResult())
                .build();

        return new ResponseEntity<>(errorDetails, errorDetails.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("REST API constraint violation error: {}", ex.getMessage());

        RestErrorDetails error = RestErrorDetails.constraintBuilder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Constraint violation.")
                .constraintViolationException(ex)
                .build();

        return new ResponseEntity<>(error, error.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {
        log.error("REST API type mismatch error: {}", ex.getMessage());

        RestError error = getFailedToConvertError(ex.getPropertyName(), ex.getValue(), ex.getRequiredType());

        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("REST API method argument type mismatch error: {}", ex.getMessage());

        RestError error = getFailedToConvertError(ex.getName(), ex.getValue(), ex.getRequiredType());

        return new ResponseEntity<>(error, error.getStatus());
    }

    private RestError getFailedToConvertError(@Nullable String propertyName,
                                              @Nullable Object actualValue,
                                              @Nullable Class<?> expectedClass) {
        String errorMessageTemplate = "Failed to convert parameter{}{}{}.";

        String propertyNamePart = propertyName != null
                ? getMessage(" '{}'", propertyName)
                : "";

        String actualTypePart = actualValue != null
                ? getMessage(" of type '{}'", ClassUtils.getSimpleName(actualValue))
                : "";

        String expectedTypePart = expectedClass != null
                ? getMessage(" to required type '{}'", ClassUtils.getSimpleName(expectedClass))
                : "";

        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(getMessage(errorMessageTemplate, propertyNamePart, actualTypePart, expectedTypePart))
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        log.error("REST API miss servlet request parameter error: {}", ex.getMessage());

        RestError error = RestError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(
                        getMessage("'{}' parameter is missing.",
                                ex.getParameterName()
                        )
                )
                .build();

        return new ResponseEntity<>(error, error.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        log.error("REST API miss servlet request part error: {}", ex.getMessage());

        RestError error = RestError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(
                        getMessage("'{}' part is missing.",
                                ex.getRequestPartName()
                        )
                )
                .build();

        return new ResponseEntity<>(error, error.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        log.error("REST API handler not found error: {}", ex.getMessage());

        RestError error = RestError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(
                        getMessage(
                                "No handler found for {} {}.",
                                ex.getHttpMethod(), ex.getRequestURL()
                        )
                )
                .build();

        return new ResponseEntity<>(error, error.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        log.error("REST API method not supported error: {}", ex.getMessage());

        String errorMessage = getMessage(
                "{} method not supported for this request.",
                ex.getMethod()
        );

        if (ex.getSupportedHttpMethods() != null) {
            errorMessage += getMessage(
                    " Supported HTTP methods are: {}.",
                    ex.getSupportedHttpMethods().stream().map(Enum::name).collect(Collectors.joining(", "))
            );
        }

        RestError error = RestError.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(error, error.getStatus());
    }

    // Else

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<RestError> handleException(Throwable t) {
        Throwable rootCause = ExceptionUtils.getRootCause(t);
        Throwable rootException = rootCause != null ? rootCause : t;

        HttpStatus status = defineStatus(rootException);
        UUID code = UUID.randomUUID();
        String message = rootException.getMessage();

        log.error(getMessage("REST API request failed: {}.", code), rootException);

        RestError error = RestError.builder()
                .status(status)
                .code(code)
                .message(StringUtils.isBlank(message) ? "Internal server error." : message)
                .build();

        return new ResponseEntity<>(error, error.getStatus());
    }

    private HttpStatus defineStatus(Throwable t) {
        if (t instanceof RestException) {
            return ((RestException) t).getHttpStatus();
        }

        return defineCustomStatus(t).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected Optional<HttpStatus> defineCustomStatus(Throwable t) {
        return Optional.empty();
    }

    protected String getMessage(String messagePattern, Object... args) {
        return new FormattedMessage(messagePattern, args).getFormattedMessage();
    }
}
