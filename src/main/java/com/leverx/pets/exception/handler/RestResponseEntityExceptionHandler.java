package com.leverx.pets.exception.handler;

import com.leverx.pets.exception.ApiError;
import com.leverx.pets.exception.custom.EntityNotFoundException;
import com.leverx.pets.exception.custom.SimilarPersonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
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

import static com.leverx.pets.exception.ApiError.*;
import static com.leverx.pets.util.ExceptionMessageUtil.ENTITY_EXCEPTION_MESSAGE;
import static com.leverx.pets.util.ExceptionMessageUtil.INTERNAL_ERROR_MESSAGE;
import static com.leverx.pets.util.ExceptionMessageUtil.INVALID_FORMAT_MESSAGE;
import static com.leverx.pets.util.ExceptionMessageUtil.URL_NOT_FOUND_MESSAGE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MESSAGE_PATTERN = "%s: %s";

    //404
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("405 Status Code: ", exception);

        ApiError apiError = builder()
                .status(status)
                .message(format(MESSAGE_PATTERN, URL_NOT_FOUND_MESSAGE, exception.getLocalizedMessage()))
                .build();
        return handleExceptionInternal(exception, apiError, headers, status, request);
    }

    //400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("400 Status Code: ", exception);

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
        log.debug("400 Status Code: ", exception);

        ApiError apiError = builder()
                .status(status)
                .message(format(MESSAGE_PATTERN, INVALID_FORMAT_MESSAGE, exception.getLocalizedMessage()))
                .errors(exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(toList()))
                .build();
        return handleExceptionInternal(exception, apiError, headers, status, request);
    }

    // 404
    @ExceptionHandler(SimilarPersonException.class)
    public ResponseEntity<Object> handleSwappingOwnersException(final SimilarPersonException exception) {
        log.debug("404 Status Code: ", exception);

        ApiError apiError = builder()
                .status(BAD_REQUEST)
                .message(exception.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    // 404
    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEntityException(final RuntimeException exception) {
        log.debug("404 Status Code: ", exception);

        ApiError apiError = builder()
                .status(NOT_FOUND)
                .message(format(MESSAGE_PATTERN, ENTITY_EXCEPTION_MESSAGE, exception.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternal(final RuntimeException exception) {
        log.debug("500 Status Code: ", exception);

        ApiError apiError = builder()
                .status(INTERNAL_SERVER_ERROR)
                .message(format(MESSAGE_PATTERN, INTERNAL_ERROR_MESSAGE, exception))
                .build();
        return new ResponseEntity<>(apiError, INTERNAL_SERVER_ERROR);
    }
}
