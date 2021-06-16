package com.leverx.pets.exception.handler;

import com.leverx.pets.exception.ApiError;
import com.leverx.pets.exception.custom.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.leverx.pets.exception.ApiError.builder;
import static com.leverx.pets.util.ExceptionMessageUtil.ENTITY_EXCEPTION;
import static com.leverx.pets.util.ExceptionMessageUtil.INTERNAL_ERROR;
import static com.leverx.pets.util.ExceptionMessageUtil.INVALID_FORMAT;
import static com.leverx.pets.util.ExceptionMessageUtil.URL_NOT_FOUND;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_PATTERN = "%s: %s";

    //404
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("405 Status Code", exception);

        ApiError apiError = builder()
                .status(status)
                .message(format(ERROR_PATTERN, URL_NOT_FOUND, exception.getLocalizedMessage()))
                .build();
        return handleExceptionInternal(exception, apiError, headers, status, request);
    }

    //400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("400 Status Code", exception);

        ApiError apiError = builder()
                .status(status)
                .message(exception.getLocalizedMessage())
                .errors(exception
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(toList()))
                .build();
        return handleExceptionInternal(exception, apiError, headers, status, request);
    }

    // 400
    @Override
    protected ResponseEntity<Object> handleBindException(final BindException exception, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.debug("400 Status Code", exception);

        ApiError apiError = builder()
                .status(status)
                .message(format(ERROR_PATTERN, INVALID_FORMAT, exception.getLocalizedMessage()))
                .errors(exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(toList()))
                .build();
        return handleExceptionInternal(exception, apiError, headers, status, request);
    }

    // 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityException(final EntityNotFoundException exception) {
        log.debug("404 Status Code", exception);

        ApiError apiError = builder()
                .status(NOT_FOUND)
                .message(format(ERROR_PATTERN, ENTITY_EXCEPTION, exception.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternal(final RuntimeException exception) {
        log.debug("500 Status Code", exception);

        ApiError apiError = builder()
                .status(INTERNAL_SERVER_ERROR)
                .message(format(ERROR_PATTERN, INTERNAL_ERROR, exception.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(apiError, INTERNAL_SERVER_ERROR);
    }
}
