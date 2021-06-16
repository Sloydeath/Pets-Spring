package com.leverx.pets.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Person not found")
public class PersonNotFoundException extends EntityNotFoundException {

    public PersonNotFoundException(String message) {
        super(message);
    }
}
