package com.bubalex.test.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * The base class of the REST exception handler.
 */
@Log4j2
@ControllerAdvice
public class VisaLexServerExceptionHandler extends RestExceptionHandler {

}
