package com.leverx.pets.exception.handler;

import com.leverx.pets.exception.ApiError;
import com.leverx.pets.exception.custom.PersonNotFoundException;
import com.leverx.pets.exception.custom.PetNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * This class handlers exceptions of any type
 *
 * @author Andrew Panas
 */

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    public RestResponseEntityExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("400 Status Code", ex);
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setErrors(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    // 400
    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.debug("400 Status Code", ex);
        ApiError apiError = new ApiError();
        apiError.setMessage(String.format("Invalid format: %s", ex.getMessage()));
        apiError.setErrors(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    // 404
    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<Object> handlePetNotFound(final RuntimeException ex, final WebRequest request) {
        log.debug("404 Status Code", ex);
        String bodyOfResponse = "Pet is not found";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), NOT_FOUND, request);
    }

    // 404
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Object> handlePersonNotFound(final RuntimeException ex, final WebRequest request) {
        log.debug("404 Status Code", ex);
        String bodyOfResponse = "Person is not found";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), NOT_FOUND, request);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        log.debug("500 Status Code", ex);
        ApiError apiError = new ApiError();
        apiError.setMessage("Internal error: " + ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }
}
